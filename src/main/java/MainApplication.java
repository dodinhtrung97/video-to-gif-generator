import com.rabbitmq.client.*;
import entity.RequestBody;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.FileService;
import service.impl.FileServiceImpl;
import util.Constant;

import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Worker started");
        Thread.sleep(5000);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Constant.QUEUE_NAME, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                this.parseInput(message);
            }

            /**
             * Parse json request and do work
             * @param jsonRequest
             */
            public void parseInput(String jsonRequest) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject = (JSONObject) parser.parse(jsonRequest);
                } catch (ParseException e) {
                    System.out.println("Failed to digest request");
                }

                String srcHost = jsonObject.get("srcHost").toString();
                String srctBucketName = jsonObject.get("srctBucketName").toString();
                String srcObjectName = jsonObject.get("srcObjectName").toString();
                String destHost = jsonObject.get("destHost").toString();
                String destBucketName = jsonObject.get("destBucketName").toString();
                String destObjectName = jsonObject.get("destObjectName").toString();

                RequestBody requestBody = new RequestBody(srcHost, srctBucketName, srcObjectName,
                        destHost, destBucketName, destObjectName);

                this.generateGif(requestBody);
            }

            /**
             * Generate gif by request
             * @param request
             */
            public void generateGif(RequestBody request) {
                FileService fileService = new FileServiceImpl();

                String videoDir = fileService.downloadFile(request.getSrcHost(), request.getSrcBucketName(), request.getSrcObjectName());
                try {
                    fileService.convertVideoToJpg(request.getDestBucketName(), videoDir, 0);
                    fileService.convertFramesToGif(request.getDestBucketName(), request.getDestObjectName());
                } catch (Exception e) {
                    System.out.println("Failed to convert video!!");
                    e.printStackTrace();
                }
            }
        };

        channel.basicConsume(Constant.QUEUE_NAME, true, consumer);
    }



}
