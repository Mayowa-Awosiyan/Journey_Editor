import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import javax.swing.undo.UndoableEdit;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isRightMouseButton;

public class Testing extends JFrame {

    mxUndoManager undoManager;

    List<UndoableEdit> changes;
    //these 2 integer values help keep track of the currently selected cell for deleting purposes
    private int currX,currY;

    private int currLayout;

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
        final mxGraph graph = new mxGraph();
        //create undo manager built in way to manage undo and redo operations
        undoManager = new mxUndoManager();
        currY=0;
        currX=0;
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();

        journeyDB= new JourneyDB();
        ArrayList<String> cells = journeyDB.getData("Select * From main_members");
        try
        {
            //adding nodes and edges to the graph
            /*
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!",
                    240, 150, 80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);


             */

            //todo give a layout so it looks nice

            int v1 = 20;
            int v2 =50;
            Object prevCell= null;
            for (String currentCell:
                 cells) {
                Object cell = graph.insertVertex(parent, null, currentCell,v1,v2,120, 50);
                v1+= 50;
                v2+= 75;

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

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);



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




        //adding context menu when clicking a Node
        final JPopupMenu context = new JPopupMenu();

        //adding context menu for when right clicking in the void, aka a node is not where right clicked occured
        final JPopupMenu voidContext = new JPopupMenu();
        JMenuItem undo = new JMenuItem("Undo");
        undo.setMnemonic(KeyEvent.VK_P);
        undo.getAccessibleContext().setAccessibleDescription("Undo");
        //adding an action to the undo button
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoManager.undo();
            }
        });
        context.add(undo);
        voidContext.add(undo);

        //copy and pasted above code to add Redo functionality
        //todo fix this
        undo = new JMenuItem("Redo");
        undo.setMnemonic(KeyEvent.VK_P);
        undo.getAccessibleContext().setAccessibleDescription("Redo");
        //adding an action to the undo button
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                undoManager.redo();
            }
        });
        context.add(undo);
        voidContext.add(undo);


        //todo add layout options
        JMenuItem menuItem = new JMenuItem("Change Layout");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Change Layout");
        //new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currLayout ==1){
                    currLayout+= 1;
                    mxFastOrganicLayout mxFastOrganicLayout = new mxFastOrganicLayout(graph);
                    mxFastOrganicLayout.setMinDistanceLimit(20);
                    mxFastOrganicLayout.execute(graph.getDefaultParent());
                }
                else if(currLayout == 2){
                    currLayout+=1;
                    mxHierarchicalLayout mxHierarchicalLayout = new mxHierarchicalLayout(graph);
                    mxHierarchicalLayout.setFineTuning(true);
                    mxHierarchicalLayout.execute(graph.getDefaultParent());
                }
                else if(currLayout == 3){
                    currLayout+=1;
                    mxOrganicLayout mxOrganicLayout = new mxOrganicLayout(graph);
                    mxOrganicLayout.setMinDistanceLimit(20);
                    mxOrganicLayout.execute(graph.getDefaultParent());
                }
                else if(currLayout ==4){
                    currLayout+=1;
                    System.out.print("CIRCLE");
                    mxCircleLayout mxCircleLayout = new mxCircleLayout(graph);
                    mxCircleLayout.setResetEdges(true);
                    mxCircleLayout.execute(graph.getDefaultParent());
                }
                else if(currLayout ==5){
                    currLayout+=1;
                    mxCompactTreeLayout mxCompactTreeLayout = new mxCompactTreeLayout(graph);
                    mxCompactTreeLayout.execute(graph.getDefaultParent());
                }
                else if(currLayout ==6){
                    currLayout=1;
                    mxPartitionLayout mxPartitionLayout = new mxPartitionLayout(graph);
                    mxPartitionLayout.execute(graph.getDefaultParent());
                }

            }
        });

        voidContext.add(menuItem);

        //adding color changing option to context menu
        //Todo: make it actually change color
        menuItem = new JMenuItem("Change Color");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Change Color");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mxCell obj = (mxCell) graphComponent.getCellAt(currX,currY);
                obj.setAttribute("color","#FF0000");
                System.out.println(obj.getAttribute("color"));
                obj.setStyle("red");
                System.out.println(obj.getStyle());


            }
        });
        context.add(menuItem);
        menuItem = new JMenuItem("Add cell");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Add Cell");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo change the postion a bit so it goes where expected
                Object cell = graph.insertVertex(parent, null, "",getMousePosition().getX(),getMousePosition().getY(),120, 50);
            }
        });
        voidContext.add(menuItem);

        //adding deleting cells to the context menu
        menuItem = new JMenuItem("Delete Cell");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Cell");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = graphComponent.getCellAt(currX, currY);

                graph.removeCells(new Object[]{obj});
            }
        });
        context.add(menuItem);


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
                    if(graphComponent.getCellAt(e.getX(),e.getY()) !=null) {
                        currX= e.getX();
                        currY= e.getY();
                        context.show(e.getComponent(),e.getX(),e.getY());
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



    }

    //Early testing to get used to all the new functons I will be using as part of this project
    public static void main(String[] args) throws SQLException {

        //todo add a dialog box to kick start the diagram (potentially always available)
        //todo make sure nodes arent repeated
        //todo add multiple select for delete AND delete all
        Testing frame = new Testing();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //sets the default size of the window
        frame.setSize(400, 320);


        //makes the window visible
        frame.setVisible(true);

    }
}
