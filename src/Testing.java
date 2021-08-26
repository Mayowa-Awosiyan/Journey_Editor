/*
Copyright (c) 2001-2014, JGraph Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the JGraph nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL JGRAPH BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
© 2021 GitHub, Inc.

 */

import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.io.mxGdCodec;
import com.mxgraph.io.mxModelCodec;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static javax.swing.SwingUtilities.isRightMouseButton;

public class Testing extends JFrame {

    mxUndoManager undoManager;

    List<UndoableEdit> changes;
    //these 2 integer values help keep track of the currently selected cell for deleting purposes
    private int currX,currY;
    //variable to hold the id of all Data Entries will be very important
    private ProgressingLabel label;
    private int customs;
    private ArrayList<DataEntry> nodes;

    private int currLayout;
    private ArrayList<Object> cellList;
    private ArrayList<MemberEntry> journeyDBmembers;
    private ArrayList<GrantEntry> dBgrants;
    private ArrayList<EventEntry> eventsDB;
    private ArrayList<PartnerEntry> partnerDB;
    private ArrayList<ProductEntry> productDB;


    JourneyDB journeyDB;
    //setting up undohandler that will keep track of edits
    protected mxEventSource.mxIEventListener undoHandler = new mxEventSource.mxIEventListener() {
        @Override
        public void invoke(Object o, mxEventObject mxEventObject) {
            undoManager.undoableEditHappened((mxUndoableEdit) mxEventObject.getProperty("edit"));
        }
    };


    public Testing() throws SQLException {
        //setting the title of the window getting opened by the program
        super("The new goal");
        currLayout= 1;
        customs= 1;
        final mxGraph graph = new mxGraph();
        //create undo manager built in way to manage undo and redo operations
        undoManager = new mxUndoManager();
        currY=0;
        currX=0;
        label= new ProgressingLabel("Label");
        nodes= new ArrayList<>();
        nodes.add(new DataEntry("Graph",null));
        cellList = new ArrayList<>();
        cellList.add("Start Point");



        //Stylesheets that give nodes representing different data types different appearances
        //member stylesheet
        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> memberStyle = new Hashtable<String, Object>();
        memberStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        memberStyle.put(mxConstants.STYLE_OPACITY, 50);
        memberStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        stylesheet.putCellStyle("Member", memberStyle);
        //grant style sheet
        Hashtable<String, Object> grantStyle = new Hashtable<String, Object>();
        grantStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        grantStyle.put(mxConstants.STYLE_OPACITY, 50);
        grantStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        grantStyle.put(mxConstants.STYLE_FILLCOLOR,"#F5793A");
        stylesheet.putCellStyle("Grant", grantStyle);

        //event style sheet
        Hashtable<String, Object> eventStyle = new Hashtable<String, Object>();
        eventStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_HEXAGON);
        eventStyle.put(mxConstants.STYLE_OPACITY, 50);
        eventStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        eventStyle.put(mxConstants.STYLE_FILLCOLOR, "#A95AA1");
        eventStyle.put(mxConstants.STYLE_STROKECOLOR,"#BA5AA1");
        stylesheet.putCellStyle("Event", eventStyle);

        Hashtable<String, Object> productStyle = new Hashtable<String, Object>();
        productStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CLOUD);
        productStyle.put(mxConstants.STYLE_OPACITY, 50);
        productStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        productStyle.put(mxConstants.STYLE_FILLCOLOR, "#0F2080");
        stylesheet.putCellStyle("Product", productStyle);

        Hashtable<String, Object> customStyle = new Hashtable<String, Object>();
        customStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        customStyle.put(mxConstants.STYLE_OPACITY, 50);
        customStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        customStyle.put(mxConstants.STYLE_FILLCOLOR, "#FFFF00");
        customStyle.put(mxConstants.STYLE_STROKECOLOR, "#FFFF00");
        stylesheet.putCellStyle("Custom", customStyle);

        journeyDB= new JourneyDB();
        journeyDBmembers = journeyDB.getMembers("Select * From main_members");
        dBgrants = journeyDB.getGrants("select * from main_grants");
        eventsDB = journeyDB.getEvents("select * from main_events");
        partnerDB = journeyDB.getPartners("select * from main_partners");
        productDB = journeyDB.getProducts("Select * from main_products");

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        try
        {
            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (MemberEntry currentCell:
                    journeyDBmembers) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Member");

                v1+= 50;
                v2+= 75;
                nodes.add(currentCell);
                cellList.add(cell);
                label.progress();
                if(prevCell != null){
                    graph.insertEdge(parent,null, "",prevCell,cell);
                }
                prevCell = cell;
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        //add listeners so the program can keep track of changes
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);
        undoHandler = new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object o, mxEventObject mxEventObject) {
                //undoManager.undoableEditHappened((mxUndoableEdit) mxEventObject.getProperty("edit"));
                List<mxUndoableEdit.mxUndoableChange> changes = ((mxUndoableEdit) mxEventObject.getProperty("edit")).getChanges();
                graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
            }
        };

        undoManager.addListener(mxEvent.UNDO, undoHandler);
        undoManager.addListener(mxEvent.REDO,undoHandler);

        //adding context menu when clicking a member/event/partner/etc. Node
        final JPopupMenu memberContext = new JPopupMenu();
        final JPopupMenu eventContext = new JPopupMenu();
        final JPopupMenu productContext = new JPopupMenu();
        final JPopupMenu grantContext = new JPopupMenu();
        final JPopupMenu partnerContext = new JPopupMenu();

        //adding context menu for when right clicking in the void, aka a node is not where right clicked occurred
        final JPopupMenu voidContext = new JPopupMenu();

        JMenuItem undo = new JMenuItem("Undo");
        undo.setMnemonic(KeyEvent.VK_Z);
        undo.getAccessibleContext().setAccessibleDescription("Undo");
        //adding an action to the undo button
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoManager.undo();
            }
        });

        voidContext.add(undo);

        //copy and pasted above code to add Redo functionality

        undo = new JMenuItem("Redo");
        undo.setMnemonic(KeyEvent.VK_R);
        undo.getAccessibleContext().setAccessibleDescription("Redo");
        //adding an action to the undo button
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                undoManager.redo();
            }
        });

        voidContext.add(undo);

        JMenuItem menuItem = new JMenuItem("Change Layout");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Change Layout");
        //new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currLayout == 1){
                    currLayout+=1;
                    mxHierarchicalLayout mxHierarchicalLayout = new mxHierarchicalLayout(graph);
                    //makes layout easier to follow when complexity rises
                    mxHierarchicalLayout.setFineTuning(true);
                    mxHierarchicalLayout.setInterHierarchySpacing(40);
                    mxHierarchicalLayout.setParallelEdgeSpacing(30);
                    mxHierarchicalLayout.execute(graph.getDefaultParent());
                }
                else if(currLayout ==2){
                    currLayout=1;
                    mxCompactTreeLayout mxCompactTreeLayout = new mxCompactTreeLayout(graph);
                    mxCompactTreeLayout.setNodeDistance(30);
                    mxCompactTreeLayout.setGroupPadding(30);
                    mxCompactTreeLayout.execute(graph.getDefaultParent());
                }
            }
        });
        voidContext.add(menuItem);

        //adding the ability to add new cells to the journey through the context menu
        menuItem = new JMenuItem("Add cell");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Add Cell");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object parent = graph.getDefaultParent();
                Object cell = graph.insertVertex(parent, label.toString(), "", getMousePosition(true).getX(),
                        getMousePosition(true).getY(),120, 50,"Custom");
                label.progress();

                nodes.add(new CustomEntry(null,String.valueOf(customs), ""));
                customs++;
                cellList.add(cell);
            }
        });
        voidContext.add(menuItem);

        //adding deleting cells to the context menu
        menuItem = new JMenuItem("Delete Selected Cell(s)");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Selected Cell(s)");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puts all currently selected cells into a variable
                Object[] var = graph.getSelectionCells();
                graph.removeCells(var);
                Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
                if(vertices.length ==0){
                    String[] choices =new String[] {"Members", "Products", "Grants","Partners","Events"};
                    String selection = (String) JOptionPane.showInputDialog(graphComponent,"Choose what to populate your graph","choices",
                            JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
                    choosePopulation(graph, selection);
                }
            }
        });
        memberContext.add(menuItem);
        menuItem = new JMenuItem("Delete Selected Cell(s)");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Selected Cell(s)");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puts all currently selected cells into a variable
                Object[] var = graph.getSelectionCells();
                graph.removeCells(var);
                Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
                if(vertices.length ==0){
                    String[] choices =new String[] {"Members", "Products", "Grants","Partners","Events"};
                    String selection = (String) JOptionPane.showInputDialog(graphComponent,"Choose what to populate your graph","choices",
                            JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
                    choosePopulation(graph, selection);
                }
            }
        });
        grantContext.add(menuItem);
        menuItem = new JMenuItem("Delete Selected Cell(s)");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Selected Cell(s)");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puts all currently selected cells into a variable
                Object[] var = graph.getSelectionCells();
                graph.removeCells(var);
                Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
                if(vertices.length ==0){
                    String[] choices =new String[] {"Members", "Products", "Grants","Partners","Events"};
                    String selection = (String) JOptionPane.showInputDialog(graphComponent,"Choose what to populate your graph","choices",
                            JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
                    choosePopulation(graph, selection);
                }
            }
        });
        eventContext.add(menuItem);

        menuItem = new JMenuItem("Delete Selected Cell(s)");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Selected Cell(s)");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puts all currently selected cells into a variable
                Object[] var = graph.getSelectionCells();
                graph.removeCells(var);
                Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
                if(vertices.length ==0){
                    String[] choices =new String[] {"Members", "Products", "Grants","Partners","Events"};
                    String selection = (String) JOptionPane.showInputDialog(graphComponent,"Choose what to populate your graph","choices",
                            JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
                    choosePopulation(graph, selection);
                }
            }
        });
        productContext.add(menuItem);
        menuItem = new JMenuItem("Delete Selected Cell(s)");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Selected Cell(s)");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puts all currently selected cells into a variable
                Object[] var = graph.getSelectionCells();
                graph.removeCells(var);
                Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
                if(vertices.length ==0){
                    String[] choices =new String[] {"Members", "Products", "Grants","Partners","Events"};
                    String selection = (String) JOptionPane.showInputDialog(graphComponent,"Choose what to populate your graph","choices",
                            JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
                    choosePopulation(graph, selection);
                }
            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Clear All");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete All");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                String[] choices =new String[] {"Members", "Products", "Grants","Partners","Events"};
                String selection = (String) JOptionPane.showInputDialog(graphComponent,"Choose what to populate your graph","choices",
                        JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
                choosePopulation(graph, selection);
            }
        });
        voidContext.add(menuItem);



        //adding reactions to certain clicks
        graphComponent.getGraphControl().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //checks to see if a right click occurred
                if(isRightMouseButton(e)){
                    //displays the context menu where the user clicked
                    //todo: make completely separate popup menu for right clicking a cell
                    if(graphComponent.getCellAt(e.getX(),e.getY()) == null){
                        voidContext.show(e.getComponent(),e.getX(),e.getY());
                    }
                    else if(graphComponent.getCellAt(e.getX(),e.getY()) !=null) {
                        currX= e.getX();
                        currY= e.getY();
                        Object chosen = nodes.get(cellList.indexOf(graphComponent.getCellAt(e.getX(), e.getY()))).getClass();
                        if(chosen == MemberEntry.class) {
                            memberContext.show(e.getComponent(), e.getX(), e.getY());
                        }
                        else if(chosen == GrantEntry.class){
                            grantContext.show(e.getComponent(),e.getX(),e.getY());
                        }
                        else if(chosen == EventEntry.class){
                            eventContext.show(e.getComponent(),e.getX(),e.getY());
                        }
                        else if(chosen == PartnerEntry.class){
                            partnerContext.show(e.getComponent(),e.getX(),e.getY());
                        }
                        else if(chosen == ProductEntry.class){
                            productContext.show(e.getComponent(),e.getX(),e.getY());
                        }
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        }
        );

        //functionality to show dates/phone numbers/emails/etc.
        //todo add keyboard shortcuts
        menuItem = new JMenuItem("Toggle Dates");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Dates");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,0);
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Emails");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Emails");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,1);
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Business Name");
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Business Name");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,2);
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Faculty");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Faculty");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,3);
            }
        });

        memberContext.add(menuItem);
        menuItem = new JMenuItem("Toggle Phone Number");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Phone Number");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,4);
            }
        });

        memberContext.add(menuItem);

        menuItem = new JMenuItem("Toggle City");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle City");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,5);
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Amount");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Amount");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,6);
            }
        });
        grantContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Received Date");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Received Date");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,7);
            }
        });
        grantContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Finish Date");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Finish Date");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,8);
            }
        });
        grantContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Type");
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Type");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,12);
            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Scope");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Scope");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,13);
            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Show Events");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Events");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphEvents(graph,"Member");

            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Show Events");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Events");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphEvents(graph,"Partner");

            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Show Members");
        menuItem.setMnemonic(KeyEvent.VK_M);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Members");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphMembers(graph,"Partner");

            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Français");
        menuItem.setMnemonic(KeyEvent.VK_F);
        menuItem.getAccessibleContext().setAccessibleDescription("Français");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,14);

            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Show Products");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Products");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphProducts(graph,"Partner");

            }
        });
        partnerContext.add(menuItem);

        menuItem = new JMenuItem("Show Dates");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Dates");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,0);

            }
        });
        eventContext.add(menuItem);

        menuItem = new JMenuItem("Show Events");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Events");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphEvents(graph,"Grant");

            }
        });
        grantContext.add(menuItem);

        menuItem = new JMenuItem("Show Grant Origin");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Grant Source");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,9);
            }
        });
        grantContext.add(menuItem);

        menuItem = new JMenuItem("Show Grant Status");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Grant Status");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,10);
            }
        });
        grantContext.add(menuItem);

        menuItem = new JMenuItem("Show If Connected to LRI");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Grant Source");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,11);
            }
        });
        grantContext.add(menuItem);



        menuItem = new JMenuItem("Show Partners");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Partners");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphPartners(graph, "Member");

            }
        });
        memberContext.add(menuItem);


        menuItem = new JMenuItem("Show Grants");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Grants");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphGrants(graph, "Member");

            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Show Grants");
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Grants");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphGrants(graph, "Event");
            }
        });
        eventContext.add(menuItem);

        menuItem = new JMenuItem("Show Members");
        menuItem.setMnemonic(KeyEvent.VK_M);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Members");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphMembers(graph, "Event");
            }
        });
        eventContext.add(menuItem);

        menuItem = new JMenuItem("Show Members");
        menuItem.setMnemonic(KeyEvent.VK_M);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Members");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphMembers(graph, "Grant");
            }
        });
        grantContext.add(menuItem);



        menuItem = new JMenuItem("Show Products");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Products");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphProducts(graph,"Member");
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Save Graph");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription("Save Graph");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ;
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Export Image");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.getAccessibleContext().setAccessibleDescription("Export Image");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
                try {
                    //todo add save dialog box
                    ImageIO.write(image, "PNG", new File("C:\\temp\\graph.png"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        voidContext.add(menuItem);

        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Save as");

        menuItem = new JMenuItem("Save File");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription("Save File");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo make the save file usable
                int userSelection = fileChooser.showSaveDialog(graphComponent);
                File fileToSave = fileChooser.getSelectedFile();
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    /*
                    if(String.valueOf(fileToSave).contains(".pdf")){

                        try {
                            PdfExport pdfExport = new PdfExport(graph,fileToSave);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    else{
                        try {
                            PdfExport pdfExport = new PdfExport(graph,new File(fileToSave + ".pdf"));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                     */

                    String filename=fileToSave.getAbsolutePath().toString();
                    mxCodecRegistry.register(new mxModelCodec( new mxGraphModel()));
                    try {

                        mxGraph graph = graphComponent.getGraph();

                        // taken from EditorActions class
                        mxCodec codec = new mxCodec();
                        String xml = mxXmlUtils.getXml(codec.encode(graph.getModel()));
                        mxUtils.writeFile(xml,filename );

                        JOptionPane.showMessageDialog( graphComponent, "File saved to: " + filename);

                    } catch( Exception ex) {
                        throw new RuntimeException( ex);
                    }
                }
            }
        });

        voidContext.add(menuItem);

        menuItem = new JMenuItem("Load Graph");
        menuItem.getAccessibleContext().setAccessibleDescription("Load Graph");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser jFileChooser= new JFileChooser();
                int returnValue = jFileChooser.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    try {
                        byte c;
                        FileInputStream fileInputStream = new FileInputStream(selectedFile);
                        while((c = (byte) fileInputStream.read()) != -1){
                            mxGdCodec.decode(String.valueOf(c),graph);
                            System.out.println(c);
                        }
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    graph.refresh();
                }
            }
        });
        voidContext.add(menuItem);


    }
/*
    public void chooseClass(String selection){

        switch (selection){
            case "Members":
                return MemberEntry.class;
            case "Products":
                return ProductEntry.class;
            case "Partners":
                return PartnerEntry.class;
            case "Grants":
                return GrantEntry.class;
            case "Events":
                return EventEntry.class;
            default:
                return MemberEntry.class;
        }
    }

 */
    public void choosePopulation(mxGraph graph, String selection){
        switch (selection){
            case "Members":
                populateMembers(graph);
                break;
            case "Products":
                populateProducts(graph);
                break;
            case "Partners":
                populatePartners(graph);
                break;
            case "Grants":
                populateGrants(graph);
                break;
            case "Events":
                populateEvents(graph);
                break;
            default:
                populateMembers(graph);
                break;
        }
    }
    //todo see if returning chooseclass into function allows for 1 funtion
    public void populateMembers(mxGraph graph) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        try
        {
            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (MemberEntry currentCell:
                    journeyDBmembers) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Member");

                v1+= 50;
                v2+= 75;
                nodes.add(currentCell);
                cellList.add(cell);
                label.progress();
                if(prevCell != null){
                    graph.insertEdge(parent,null, "",prevCell,cell);
                }
                prevCell = cell;
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }
    public void populateGrants(mxGraph graph) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        try
        {
            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (GrantEntry currentCell:
                    dBgrants) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Grant");

                v1+= 50;
                v2+= 75;
                nodes.add(currentCell);
                cellList.add(cell);
                label.progress();
                if(prevCell != null){
                    graph.insertEdge(parent,null, "",prevCell,cell);
                }
                prevCell = cell;
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }
    public void populateProducts(mxGraph graph) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        try
        {
            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (ProductEntry currentCell:
                    productDB) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Product");

                v1+= 50;
                v2+= 75;
                nodes.add(currentCell);
                cellList.add(cell);
                label.progress();
                if(prevCell != null){
                    graph.insertEdge(parent,null, "",prevCell,cell);
                }
                prevCell = cell;
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }
    public void populateEvents(mxGraph graph) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        try
        {
            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (EventEntry currentCell:
                    eventsDB) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Event");

                v1+= 50;
                v2+= 75;
                nodes.add(currentCell);
                cellList.add(cell);
                label.progress();
                if(prevCell != null){
                    graph.insertEdge(parent,null, "",prevCell,cell);
                }
                prevCell = cell;
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }
    public void populatePartners(mxGraph graph) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        try
        {
            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (PartnerEntry currentCell:
                    partnerDB) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Partner");

                v1+= 50;
                v2+= 75;
                nodes.add(currentCell);
                cellList.add(cell);
                label.progress();
                if(prevCell != null){
                    graph.insertEdge(parent,null, "",prevCell,cell);
                }
                prevCell = cell;
            }
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }

    //functions that add the right nodes to the graph based on user request
    public void updateGraphEvents(mxGraph graph, String role){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();

        for(Object addition : targets){
            if(((mxCell) addition).isEdge()){
                continue;
            }
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();

            ArrayList<EventEntry> cells = journeyDB.getEvents("Select * From main_events, relp_Event_" +role+
                    " where " + targetID+ " = relp_Event_"+ role +"."+role+"_ID and main_events.id = relp_event_"+ role+".Event_id");
            try
            {
                int v1 = 200;
                int v2 =50;

                for (EventEntry currentCell:
                        cells) {

                    if(nodes.contains(currentCell)){

                        int target =nodes.indexOf(currentCell);
                        //wont create new edges that are identical to existing ones
                        if(graph.getEdgesBetween(addition,cellList.get(target)).length > 0){
                            ;
                        }
                        else {
                            graph.insertEdge(parent,null,"",addition,cellList.get(target));
                        }
                    }
                    else {
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Event");
                        v1+= 50;
                        v2+= 75;
                        label.progress();
                        nodes.add(currentCell);
                        cellList.add(cell);
                        graph.insertEdge(parent,null, "",addition,cell);

                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void updateGraphGrants(mxGraph graph, String role){

        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            if(((mxCell) addition).isEdge()){
                continue;
            }
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();

            ArrayList<GrantEntry> cells;
            System.out.println(role.compareTo("Event"));
            if(role.compareTo("Event") == 0){
               cells = journeyDB.getGrants("Select * From main_grants, relp_Event_Grant"+
                        " where " + targetID+ " = relp_Event_Grant.Event_ID and main_grants.id = relp_Event_Grant.grant_id");
            }
            else{
                cells = journeyDB.getGrants("Select * From main_grants, relp_grant_" + role+
                        " where " + targetID+ " = relp_grant_"+role+"."+role+"_ID and main_grants.id = relp_grant_"+role+".grant_id");
            }

            try
            {
                int v1 = 200;
                int v2 =50;

                for (GrantEntry currentCell:
                        cells) {

                    if(nodes.contains(currentCell)){
                        int target =nodes.indexOf(currentCell);
                        //wont create new edges that are identical to existing ones
                        if(graph.getEdgesBetween(addition,cellList.get(target)).length > 0){
                            ;
                        }
                        else {
                            graph.insertEdge(parent,null,"",addition,cellList.get(target));
                        }
                    }
                    else {
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Grant");
                        v1+= 50;
                        v2+= 75;
                        label.progress();
                        nodes.add(currentCell);
                        cellList.add(cell);
                        graph.insertEdge(parent,null, "",addition,cell);

                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void updateGraphProducts(mxGraph graph, String role){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            if(((mxCell) addition).isEdge()){
                continue;
            }
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            ArrayList<ProductEntry> cells;
            targetID = nodes.get(label.getTarget(targetID)).getId();
            if(role.compareTo("Event") == 0){
               cells = journeyDB.getProducts("Select * From main_products, relp_Event_product"+
                        " where " + targetID+ " = relp_Event_product.Event_ID and main_products.id = relp_Event_product.product_id");
            }
            else{
                cells= journeyDB.getProducts("Select * From main_Products, relp_Product_"+role +
                        " where " + targetID+ " = relp_product_"+role+"."+role+"_ID and main_products.id = relp_product_"+role+".product_id");
            }
            try
            {
                int v1 = 200;
                int v2 =50;

                for (ProductEntry currentCell:
                        cells) {

                    if(nodes.contains(currentCell)){
                        int target =nodes.indexOf(currentCell);
                        //wont create new edges that are identical to existing ones
                        if(graph.getEdgesBetween(addition,cellList.get(target)).length > 0){
                            ;
                        }
                        else {
                            graph.insertEdge(parent,null,"",addition,cellList.get(target));
                        }
                    }
                    else {
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Product");
                        v1+= 50;
                        v2+= 75;
                        label.progress();
                        nodes.add(currentCell);
                        cellList.add(cell);
                        graph.insertEdge(parent,null, "",addition,cell);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void updateGraphPartners(mxGraph graph, String role){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();
            ArrayList<PartnerEntry> cells;
            if(role.compareTo("Member") == 0){
                cells = journeyDB.getPartners("Select * From main_partners, relp_Partner_member"+
                        " where " + targetID+ " = relp_Partner_Member.member_ID and main_partners.id = relp_partner_member.partner_id");
            }
            else{
                cells = journeyDB.getPartners("Select * From main_Partners, relp_"+role+"_Partner" +
                        " where " + targetID+ " = relp_"+role+"_partner."+ role +"_id and main_partners.id = relp_"+role+"_partner.partner_id");
            }
            try
            {
                int v1 = 200;
                int v2 =50;

                for (PartnerEntry currentCell:
                        cells) {

                    if(nodes.contains(currentCell)){
                        int target =nodes.indexOf(currentCell);
                        //wont create new edges that are identical to existing ones
                        //todo change color of 2nd and onward edges
                        if(graph.getEdgesBetween(addition,cellList.get(target)).length > 0){
                            ;
                        }
                        else {
                            graph.insertEdge(parent,null,"",addition,cellList.get(target));
                        }
                    }
                    else {
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"TRIANGLE;strokeColor=#000000;fillColor=grey");
                        v1+= 50;
                        v2+= 75;
                        label.progress();
                        nodes.add(currentCell);
                        cellList.add(cell);
                        graph.insertEdge(parent,null, "",addition,cell);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void updateGraphMembers(mxGraph graph, String role){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();

        for(Object addition : targets){
            if(((mxCell) addition).isEdge()){
                continue;
            }
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();

            ArrayList<MemberEntry> cells = journeyDB.getMembers("Select * From main_members, relp_"+role+"_member " +
                    "where " + targetID+ " = relp_"+role+"_member."+role+"_ID and main_members.id = relp_"+ role+"_member.Member_id");
            try
            {
                int v1 = 200;
                int v2 =50;

                for (MemberEntry currentCell:
                        cells) {

                    if(nodes.contains(currentCell)){

                        int target =nodes.indexOf(currentCell);
                        //wont create new edges that are identical to existing ones
                        if(graph.getEdgesBetween(addition,cellList.get(target)).length > 0){
                            ;
                        }
                        else {
                            graph.insertEdge(parent,null,"",cellList.get(target),addition);
                        }
                    }
                    else {
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,"Member");
                        v1+= 50;
                        v2+= 75;
                        label.progress();
                        nodes.add(currentCell);
                        cellList.add(cell);
                        graph.insertEdge(parent,null, "",cell,addition);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    //function to display requested info
    public void displayInfo(mxGraph graph,int choice){
        int count = 0;

        for (DataEntry currNode:
                nodes) {

            switch (choice){
                case 0:
                    currNode.toggleDate();
                    break;
                case 1:
                    currNode.toggleEmail();
                    break;
                case 2:
                    currNode.toggleBusiness();
                    break;
                case 3:
                    currNode.toggleFaculty();
                    break;
                case 4:
                    currNode.togglePhone();
                    break;
                case 5:
                    currNode.toggleCity();
                    break;
                case 6:
                    currNode.toggleAmount();
                    break;
                case 7:
                    currNode.toggleRecDate();
                    break;
                case 8:
                    currNode.toggleFinDate();
                    break;
                case 9:
                    currNode.toggleSource();
                    break;
                case 10:
                    currNode.toggleStatus();
                    break;
                case 11:
                    currNode.toggleLRI();
                    break;
                case 12:
                    currNode.toggleType();
                    break;
                case 13:
                    currNode.toggleScope();
                    break;
                case 14:
                    currNode.toggleFrench();
                    break;
            }
        }
        int index =1;
        for(Object target : graph.getChildVertices(graph.getDefaultParent())){
            //prevents the contents of custom nodes from being deleted
            if(((mxCell) target).getStyle().equals("Custom")){
                int pointer = cellList.indexOf(target);
                nodes.get(pointer).setContent(String.valueOf(((mxCell) target).getValue()));
            }
            ((mxCell) target).setValue(nodes.get(index).toString());

            //change the size of cells based off of how many lines
            count = target.toString().split("\r\n|\r|\n").length -1;
            ((mxCell) target).getGeometry().setHeight(50 + 7*count);

            index++;
        }
        graph.refresh();
    }




    //Early testing to get used to all the new functions I will be using as part of this project
    public static void main(String[] args) throws SQLException {

        //todo add a dialog box to kick start the diagram (potentially always available)

        Testing frame = new Testing();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //sets the default size of the window default 400, 320
        frame.setSize(700, 620);
        //makes the window visible
        frame.setVisible(true);


        //5-6 colorblind friendly colors
        //5-6 shapes for different types (member, event, etc.)
        //create edge context menu to create color based paths

        //look into serialization for saving/exporting/loading
        //look into JSON for saving/loading graphs as well
        //text based beats binary based
    }
}
