/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromossomes.ohyeh;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import org.paim.commons.Image;
import org.paim.commons.ImageFactory;
import visnode.application.NodeNetwork;
import visnode.executor.EditNodeDecorator;
import visnode.executor.Node;
import visnode.executor.ProcessNode;

/**
 *
 * @author Pichau
 */
public class NetworkEvaluator {
    
    static Image inputImage;
    static Image expeceted;
    static {
        try {
            inputImage = ImageFactory.buildRGBImage(ImageIO.read(ImageComparer.class.getResource("/Test_RGB/26.bmp")));
            expeceted = ImageFactory.buildRGBImage(ImageIO.read(ImageComparer.class.getResource("/Test_Labels/26.bmp")));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private CompositeDisposable compositeSubscription = new CompositeDisposable();
    
    public CompletableFuture<IndividualResult> evaluate(NodeNetwork network) {
        CompletableFuture<IndividualResult> future = new CompletableFuture<>();
        try {
            Iterator<EditNodeDecorator> iterator = network.getNodes().iterator();
            run(iterator, inputImage).thenAccept((img) -> {
                ImageCompareResult res = new ImageComparer().compare(img, expeceted);
                future.complete(new IndividualResult(res.getCorrectPercentage()));
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
        return future;
    }

    public CompletableFuture<Image> run(Iterator<EditNodeDecorator> iterator, Image inputImage) {
        CompletableFuture<Image> future = new CompletableFuture<>();
        Node n = iterator.next().getDecorated();
        if (!(n instanceof ProcessNode)) {
            run(iterator, inputImage).thenAccept((img2) -> future.complete(img2));
            return future;
        }
        ProcessNode node = (ProcessNode) n;
        node.setInput("image", inputImage);
        node.process((p) -> {});
        Observable<Image> obs = node.getOutput("image");
        compositeSubscription.add(obs.subscribe((x) -> {
            Image img = (Image) x;
            if (iterator.hasNext()) {
                run(iterator, img).thenAccept((img2) -> future.complete(img2));
            } else {
                compositeSubscription.clear();
                future.complete(img);
            }
        }));
        return future;
    }
    
}
