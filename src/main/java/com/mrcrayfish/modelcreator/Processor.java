package com.mrcrayfish.modelcreator;

/**
 * Author: MrCrayfish
 */
public interface Processor<T>
{
    /**
     * Processes the given argument.
     *
     * @param t the object to process
     * @return if the object was processed successfully
     */
    boolean run(T t);
}
