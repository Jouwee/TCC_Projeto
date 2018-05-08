/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chromossomes.ohyeh;

/**
 * Gene
 * 
 * @param <T>
 */
public interface Gene<T> {
    
    /**
     * Retorna o valor do Gene
     * 
     * @return T
     */
    public T value();
    
}
