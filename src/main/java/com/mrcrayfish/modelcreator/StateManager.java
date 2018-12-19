package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.ElementManagerState;

import java.util.Stack;

/**
 * Author: MrCrayfish
 */
public class StateManager
{
    /* Undo/Redo Stack */
    private static Stack<ElementManagerState> states = new Stack<>();
    private static int tailIndex = -1;

    public static void pushState(ElementManager manager)
    {
        while(tailIndex < states.size() - 1)
        {
            states.pop();
        }

        if(states.size() >= 50) //Make configurable
        {
            while(states.size() > 50)
            {
                states.remove(0);
            }
        }
        states.push(manager.createState());
        tailIndex = states.size() - 1;
    }

    public static void restorePreviousState(ElementManager manager)
    {
        if(canRestorePreviousState())
        {
            ElementManagerState state = states.get(tailIndex - 1);
            manager.restoreState(state);
            tailIndex--;
        }
    }

    public static void restoreNextState(ElementManager manager)
    {
        if(canRestoreNextState())
        {
            ElementManagerState state = states.get(tailIndex + 1);
            manager.restoreState(state);
            tailIndex++;
        }
    }

    public static boolean canRestorePreviousState()
    {
        return tailIndex > 0;
    }

    public static boolean canRestoreNextState()
    {
        return tailIndex < states.size() - 1;
    }

    public static void clear()
    {
        states.clear();
        tailIndex = -1;
    }

    public static Stack<ElementManagerState> getStates()
    {
        return states;
    }

    public static int getTailIndex()
    {
        return tailIndex;
    }
}
