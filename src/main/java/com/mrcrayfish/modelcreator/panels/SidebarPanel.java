package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.component.*;
import com.mrcrayfish.modelcreator.component.Menu;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.*;
import com.mrcrayfish.modelcreator.panels.tabs.ElementPanel;
import com.mrcrayfish.modelcreator.panels.tabs.FacePanel;
import com.mrcrayfish.modelcreator.panels.tabs.RotationPanel;
import com.mrcrayfish.modelcreator.texture.TextureEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel implements ElementManager
{
    private ModelCreator creator;

    // Swing Variables
    private SpringLayout layout;
    private DefaultListModel<ElementCellEntry> model = new DefaultListModel<>();
    private JList<ElementCellEntry> list = new JElementList();
    private JScrollPane scrollPane;
    private JPanel btnContainer;
    private JButton btnAdd = new JButton();
    private JButton btnRemove = new JButton();
    private JButton btnDuplicate = new JButton();
    private JTextField name = new JTextField();
    private CuboidTabbedPane tabbedPane = new CuboidTabbedPane(this);

    private TextureEntry particle = null;
    private boolean ambientOcc = true;

    private DisplayProperties properties = new DisplayProperties(DisplayProperties.MODEL_CREATOR_BLOCK);

    public SidebarPanel(ModelCreator creator)
    {
        this.creator = creator;
        this.setLayout(layout = new SpringLayout());
        this.setPreferredSize(new Dimension(200, 760));
        this.initComponents();
        this.setLayoutConstaints();
    }

    private void initComponents()
    {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 14);

        btnContainer = new JPanel(new GridLayout(1, 3, 4, 0));
        btnContainer.setPreferredSize(new Dimension(190, 30));

        btnAdd.setIcon(Icons.cube);
        btnAdd.setToolTipText("New Element");
        btnAdd.addActionListener(e -> this.newElement());
        btnAdd.setPreferredSize(new Dimension(30, 30));
        btnContainer.add(btnAdd);

        btnRemove.setIcon(Icons.bin);
        btnRemove.setToolTipText("Remove Element");
        btnRemove.addActionListener(e -> this.deleteElement());
        btnRemove.setPreferredSize(new Dimension(30, 30));
        btnContainer.add(btnRemove);

        btnDuplicate.setIcon(Icons.copy);
        btnDuplicate.setToolTipText("Duplicate Element");
        btnDuplicate.addActionListener(e ->
        {
            int selected = list.getSelectedIndex();
            if(selected != -1)
            {
                model.addElement(new ElementCellEntry(new Element(model.getElementAt(selected).getElement())));
                list.setSelectedIndex(model.getSize() - 1);
                StateManager.pushState(creator.getElementManager());
            }
        });
        btnDuplicate.setFont(defaultFont);
        btnDuplicate.setPreferredSize(new Dimension(30, 30));
        btnContainer.add(btnDuplicate);
        add(btnContainer);

        name.setPreferredSize(new Dimension(190, 30));
        name.setToolTipText("Element Name");
        name.setEnabled(false);
        name.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    updateName();
                }
            }
        });
        name.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                updateName();
            }
        });
        add(name);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(26);
        list.setModel(model);
        list.addListSelectionListener(e ->
        {
            Element selectedElement = getSelectedElement();
            if(selectedElement != null)
            {
                tabbedPane.updateValues();
                name.setEnabled(true);
                name.setText(selectedElement.getName());
                list.ensureIndexIsVisible(list.getSelectedIndex());
            }
        });
        list.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    deleteElement();
                    return;
                }
                boolean home = e.getKeyCode() == KeyEvent.VK_HOME;
                if (home || e.getKeyCode() == KeyEvent.VK_END)
                    setSelectedElement(home ? 0 : model.getSize() - 1);
            }
        });
        list.setCellRenderer(new ElementCellRenderer());

        scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(190, 170));
        add(scrollPane);

        tabbedPane.setBackground(new Color(127, 132, 145));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.add("Element", new ElementPanel(this));
        tabbedPane.add("Rotation", new RotationPanel(this));
        tabbedPane.add("Faces", new FacePanel(this));
        tabbedPane.setPreferredSize(new Dimension(190, 500));
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.addChangeListener(c ->
        {
            if(tabbedPane.getSelectedIndex() == 2)
            {
                creator.setSidebar(ModelCreator.uvSidebar);
                ModelCreator.isUVSidebarOpen = true;
            }
            else
            {
                creator.setSidebar(null);
                ModelCreator.isUVSidebarOpen = false;
            }
        });
        add(tabbedPane);
    }

    public static void initIncrementButton(JButton button, Font defaultFont, String subject, boolean increase)
    {
        button.setPreferredSize(new Dimension(62, 30));
        button.setFont(defaultFont);
        button.setToolTipText(String.format("<html>%screases the %s.<br><b>Hold shift for decimals</b></html>", increase ? "In" : "De", subject));
    }

    public static void initIncrementableField(JTextField field, Font defaultFont)
    {
        field.setSize(new Dimension(62, 30));
        field.setFont(defaultFont);
        field.setHorizontalAlignment(JTextField.CENTER);
    }

    private void setLayoutConstaints()
    {
        layout.putConstraint(SpringLayout.NORTH, name, 212, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, btnContainer, 176, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, tabbedPane, 250, SpringLayout.NORTH, this);
    }

    public JList<ElementCellEntry> getList()
    {
        return list;
    }

    @Override
    public Element getSelectedElement()
    {
        int i = list.getSelectedIndex();
        if(model.getSize() > 0 && i >= 0 && i < model.getSize())
        {
            return model.getElementAt(i).getElement();
        }
        return null;
    }

    public ElementCellEntry getSelectedElementEntry()
    {
        int i = list.getSelectedIndex();
        if(model.getSize() > 0 && i >= 0 && i < model.getSize())
        {
            return model.getElementAt(i);
        }
        return null;
    }

    @Override
    public void setSelectedElement(int pos)
    {
        if(pos < model.size())
        {
            if(pos >= 0)
            {
                list.setSelectedIndex(pos);
            }
            else
            {
                list.clearSelection();
            }
            updateValues();
        }
    }

    @Override
    public List<Element> getAllElements()
    {
        List<Element> list = new ArrayList<>();
        for(int i = 0; i < model.size(); i++)
        {
            list.add(model.getElementAt(i).getElement());
        }
        return list;
    }

    @Override
    public Element getElement(int index)
    {
        //TODO null pointer exception
        return model.getElementAt(index).getElement();
    }

    @Override
    public int getElementCount()
    {
        return model.size();
    }

    @Override
    public void updateName()
    {
        String newName = name.getText();
        if(newName.isEmpty())
        {
            newName = "Cuboid";
        }
        Element selectedElement = getSelectedElement();
        if(selectedElement != null)
        {
            selectedElement.setName(newName);
            name.setText(newName);
            list.repaint();
            StateManager.pushState(creator.getElementManager());
        }
    }

    @Override
    public void updateValues()
    {
        tabbedPane.updateValues();
    }

    public ModelCreator getCreator()
    {
        return creator;
    }

    @Override
    public boolean getAmbientOcc()
    {
        return ambientOcc;
    }

    @Override
    public void setAmbientOcc(boolean occ)
    {
        ambientOcc = occ;
    }

    @Override
    public void clearElements()
    {
        model.clear();
    }

    @Override
    public void addElement(Element e)
    {
        model.addElement(new ElementCellEntry(e));
    }

    @Override
    public void setParticle(TextureEntry particle)
    {
        this.particle = particle;
    }

    @Override
    public TextureEntry getParticle()
    {
        return particle;
    }

    @Override
    public void reset()
    {
        this.clearElements();
        ambientOcc = true;
        particle = null;
    }

    @Override
    public void restoreState(ElementManagerState state)
    {
        this.reset();
        for(Element element : state.getElements())
        {
            this.model.addElement(new ElementCellEntry(new Element(element)));
        }
        this.setSelectedElement(state.getSelectedIndex());
        this.ambientOcc = state.isAmbientOcclusion();
        this.particle = state.getParticleTexture();
        this.updateValues();
    }

    @Override
    public void setDisplayProperties(DisplayProperties properties)
    {
        this.properties = new DisplayProperties(properties);
    }

    @Override
    public DisplayProperties getDisplayProperties()
    {
        return properties;
    }

    public void newElement()
    {
        model.addElement(new ElementCellEntry(new Element(1, 1, 1)));
        list.setSelectedIndex(model.size() - 1);
        StateManager.pushState(creator.getElementManager());
    }

    public void deleteElement()
    {
        int selected = list.getSelectedIndex();
        if(selected != -1)
        {
            model.remove(selected);
            name.setText("");
            name.setEnabled(false);
            tabbedPane.updateValues();
            if(selected >= list.getModel().getSize())
            {
                list.setSelectedIndex(list.getModel().getSize() - 1);
            }
            else
            {
                list.setSelectedIndex(selected);
            }
            StateManager.pushState(creator.getElementManager());
        }
    }

}
