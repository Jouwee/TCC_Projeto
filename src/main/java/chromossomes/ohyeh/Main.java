/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromossomes.ohyeh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import org.paim.commons.ImageConverter;
import visnode.application.ExceptionHandler;
import visnode.application.InvalidOpenFileException;
import visnode.application.NodeNetwork;
import visnode.application.parser.NodeNetworkParser;
import visnode.executor.EditNodeDecorator;

/**
 *
 * @author Pichau
 */
public class Main {

    GeneticAlgorithmController controller;

    public static void main(String[] args) throws InterruptedException {    
        new Main().run();
    }

    public Main() {
        controller = new GeneticAlgorithmController();
    }
    
    public void run() {
//        write();
//        if (true) return;
        
        ExceptionHandler.get().setQuiet(true);
        controller.generateStartPopulation();
        runGenerations();
        
        try {
            while (!controller.isDone()) {
                Thread.sleep(1000);
            }
        } catch (Exception r) {}
        
    }
    
    long l;
    
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    
    public void runGenerations() {
        l = System.currentTimeMillis();
        System.out.println("start");
        controller.simulateGeneration().thenAccept((res) -> {
            if (controller.isDone()) {
                System.out.println("finish");
//                write();
                return;
            }
            System.out.println("Generation " + controller.getCurrentGeneration() + " " + (System.currentTimeMillis() - l) + "ms");
            System.out.println(res);
            pool.submit(() -> loop());
        });
    }
    
    public void loop() {
        try {
            System.out.print("sleeeping...");
            Thread.sleep(1000);
            System.out.println(" ok");
        } catch (Exception e) {}
        controller.createNextGeneration();
        runGenerations();
    }
    
    public void write() {
        try {
            String dir = "C:\\Users\\Pichau\\Desktop\\gens\\";
        for (String name : new File(dir).list()) {
            String img = name.replace(".vnp", ".png");

            InputStreamReader isr = new InputStreamReader(new FileInputStream(dir + name));
            try (BufferedReader br = new BufferedReader(isr)) {
                StringBuilder sb = new StringBuilder();
                String s = br.readLine();
                while (s != null) {
                    sb.append(s);
                    s = br.readLine();
                }
                NodeNetwork network = new NodeNetworkParser().fromJson(sb.toString());

                Iterator<EditNodeDecorator> iterator = network.getNodes().iterator();
//                new NetworkEvaluator().run(iterator, NetworkEvaluator.inputImage).thenAccept((imag) -> {
//                    try {
//                        ImageIO.write(ImageConverter.toBufferedImage(imag), "png", new File(dir + img));
//                    } catch(Exception e) { e.printStackTrace(); }
//                });

            }catch (Exception ex) {
                throw new InvalidOpenFileException(ex);
            }
        }
        } catch (Exception ex) {
            throw new InvalidOpenFileException(ex);
        }
            
//            NodeNetwork n = new NodeNetworkParser().fromJson(img)
//            
//            ImageIO.write(ImageConverter.toBufferedImage(image), "png", new File("C:\\Users\\Pichau\\Desktop\\test\\" + news));
//
//        }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//        
//        NetworkEvaluator.inputImage;
//        
//            
//    public CompletableFuture<IndividualResult> evaluate(NodeNetwork network) {
//        CompletableFuture<IndividualResult> future = new CompletableFuture<>();
//        try {
//            Iterator<EditNodeDecorator> iterator = network.getNodes().iterator();
//            run(iterator, inputImage).thenAccept((img) -> {
//                ImageCompareResult res = new ImageComparer().compare(img, expeceted);
//                future.complete(new IndividualResult(res.getCorrectPercentage()));
//            });
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return future;
//    }
    }
    
}
