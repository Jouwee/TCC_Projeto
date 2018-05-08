/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromossomes.ohyeh;

import java.util.concurrent.CompletableFuture;
import visnode.application.NodeNetwork;

/**
 *
 * @author Pichau
 */
public class IndividualEvaluator {
    
        
    public CompletableFuture<IndividualResult> evaluate(Chromossome chromossome) {
        try {
            NodeNetwork network = new ChromossomeNetworkConverter().convert(chromossome);
            return new NetworkEvaluator().evaluate(network);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CompletableFuture<>();
    }
    
}
