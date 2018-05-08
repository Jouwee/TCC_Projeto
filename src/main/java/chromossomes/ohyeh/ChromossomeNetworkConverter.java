package chromossomes.ohyeh;

import java.awt.Point;
import visnode.application.NodeNetwork;
import visnode.commons.Threshold;
import visnode.executor.EditNodeDecorator;
import visnode.executor.ProcessNode;
import visnode.pdi.process.BrightnessProcess;
import visnode.pdi.process.ContrastProcess;
import visnode.pdi.process.GaussianBlurProcess;
import visnode.pdi.process.InputProcess;
import visnode.pdi.process.ThresholdProcess;

/**
 * Conversor entre cromossomos e a rede
 */
public class ChromossomeNetworkConverter {

    /** Exporta para o usuário ou só para simulação */
    private final boolean forUser;
    int x;
    
    public ChromossomeNetworkConverter() {
        this(false);
    }
    
    public ChromossomeNetworkConverter(boolean forUser) {
        this.forUser = forUser;
    }
    
    /**
     * Converte um gene em uma rede de nodos
     * 
     * @param chromossome
     * @return NodeNetwork
     */
    public NodeNetwork convert(Chromossome chromossome) {
        NodeNetwork network = new NodeNetwork();
        EditNodeDecorator last = null;
        if (forUser) {
            last = createNode(InputProcess.class, last);
            network.add(last);
        }
        Gene[] genes = chromossome.getGenes();
        for (int i = 0; i < genes.length;) {
            ProcessTypeGene pgene = (ProcessTypeGene) genes[i++];
            NumericGene[] params = new NumericGene[5];
            for (int j = 0; j < params.length; j++) {
                params[j] = (NumericGene) genes[i++];
            }
            Class c = pgene.value();
            if (c == null) {
                continue;
            }
            EditNodeDecorator node = createProcess(c, last, params);
            network.add(node);
            last = node;
        }
        network.add(createNode(ThresholdProcess.class, last));
        return network;
    }
    
    public EditNodeDecorator createProcess(Class c, EditNodeDecorator last, NumericGene... params) {
        EditNodeDecorator node = createNode(c, last);
        
        if (c == ThresholdProcess.class) {
            node.setInput("threshold", new Threshold((int) (params[0].value() * 256)));
        }
        
        if (c == BrightnessProcess.class) {
            node.setInput("brightness", (int) (params[0].value() * 512 - 256));
        }
        
        if (c == ContrastProcess.class) {
            node.setInput("contrast", params[0].value() * 3);
        }
        
        if (c == GaussianBlurProcess.class) {
            node.setInput("sigma", params[0].value() * 3);
            node.setInput("maskSize", (int)(params[1].value() * 4 + 1) * 2 + 1);
        }
        
        return node;
    }
    
    public EditNodeDecorator createNode(Class c, EditNodeDecorator last) {
        EditNodeDecorator node = new EditNodeDecorator(new ProcessNode(c));
        node.setPosition(new Point(x, 0));
        x += 250;
        if (last != null) {
            node.addConnection("image", last, "image");
        }
        return node;
    }
    
}
