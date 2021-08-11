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
    //variable to hold the id of all Data Entries will be very important todo implement this fully with an arraylist that uses id as an index
    private ProgressingLabel label;
    private ArrayList<DataEntry> nodes;

    private int currLayout;
    private String currType;
    private ArrayList<Object> cellList;


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
        label= new ProgressingLabel("Label");
        nodes= new ArrayList<>();
        nodes.add(new DataEntry("Graph",null));
        cellList = new ArrayList<>();
        cellList.add("Start Point");
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();

        journeyDB= new JourneyDB();
        ArrayList<MemberEntry> cells = journeyDB.getMembers("Select * From main_members");
        currType= "Member";
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
            for (MemberEntry currentCell:
                 cells) {
                //not having toString() causes errors that dont seem to affect the program
                Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,currType);
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

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

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

        //adding context menu when clicking a member Node
        final JPopupMenu memberContext = new JPopupMenu();

        //adding context menu for when right clicking in the void, aka a node is not where right clicked occurred
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
        memberContext.add(undo);
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
        memberContext.add(undo);
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
                obj.setAttribute("color","#FF05C4");
                System.out.println(obj.getAttribute("color"));
                obj.setStyle("red");
                System.out.println(obj.getStyle());
            }
        });
        memberContext.add(menuItem);

        //adding the ability to add new cells to the journey through the context menu
        menuItem = new JMenuItem("Add cell");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Add Cell");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo change the position a bit so it goes where expected
                Object cell = graph.insertVertex(parent, label.toString(), "",getMousePosition().getX(),
                        getMousePosition().getY(),120, 50);
                label.progress();
                //todo make this take in the data added to the cell/create class custom Entry
                nodes.add(new DataEntry(null,null));
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
            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Clear All");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete All");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
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
                    if(graphComponent.getCellAt(e.getX(),e.getY()) !=null) {
                        currX= e.getX();
                        currY= e.getY();
                        memberContext.show(e.getComponent(),e.getX(),e.getY());
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
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Toggle Emails");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfo(graph,1);
            }
        });

        memberContext.add(menuItem);

        menuItem = new JMenuItem("Toggle Business Name");
        menuItem.setMnemonic(KeyEvent.VK_P);
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


        menuItem = new JMenuItem("Show Events");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Events");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphEvents(graph);

            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Show Partners");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Partners");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphPartners(graph);

            }
        });
        memberContext.add(menuItem);


        menuItem = new JMenuItem("Show Grants");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Grants");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphGrants(graph);

            }
        });
        memberContext.add(menuItem);

        menuItem = new JMenuItem("Show Products");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription("Show Products");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGraphProducts(graph);
            }
        });
        memberContext.add(menuItem);

    }

    //functions that add the right nodes to the graph based on user request
    public void updateGraphEvents(mxGraph graph){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();
            ArrayList<EventEntry> cells = journeyDB.getEvents("Select * From main_events, relp_Event_member" +
                    " where " + targetID+ " = relp_Event_member.Member_ID and main_events.id = relp_event_member.Event_id");
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
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,currType);
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

    public void updateGraphGrants(mxGraph graph){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();
            ArrayList<GrantEntry> cells = journeyDB.getGrants("Select * From main_grants, relp_grant_member" +
                    " where " + targetID+ " = relp_grant_member.Member_ID and main_grants.id = relp_grant_member.grant_id");
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
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,currType);
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

    public void updateGraphProducts(mxGraph graph){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();
            ArrayList<ProductEntry> cells = journeyDB.getProducts("Select * From main_Products, relp_Product_member" +
                    " where " + targetID+ " = relp_product_member.Member_ID and main_products.id = relp_product_member.product_id");
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
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,currType);
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

    public void updateGraphPartners(mxGraph graph){
        Object parent = graph.getDefaultParent();
        Object[] targets = graph.getSelectionCells();
        for(Object addition : targets){
            mxCell thing = (mxCell) addition;
            //this is the id of the node
            String targetID =thing.getId();
            //this is the id in the database
            targetID = nodes.get(label.getTarget(targetID)).getId();
            ArrayList<PartnerEntry> cells = journeyDB.getPartners("Select * From main_Partners, relp_Partner_member" +
                    " where " + targetID+ " = relp_partner_member.Member_ID and main_partners.id = relp_partner_member.partner_id");
            try
            {
                int v1 = 200;
                int v2 =50;

                for (PartnerEntry currentCell:
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
                        Object cell = graph.insertVertex(parent, label.toString(), currentCell.toString(),v1,v2,120, 50,currType);
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

    //function to display requested info
    public void displayInfo(mxGraph graph,int choice){
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
            }
        }
        int index =1;
        for(Object target : graph.getChildVertices(graph.getDefaultParent())){
            ((mxCell) target).setValue(nodes.get(index).toString());
            index++;
        }
        graph.refresh();
    }



    //Early testing to get used to all the new functions I will be using as part of this project
    public static void main(String[] args) throws SQLException {

        //todo add a dialog box to kick start the diagram (potentially always available)
        //todo make sure nodes arent repeated
        //todo write up email on what to add to menus
        Testing frame = new Testing();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //sets the default size of the window default 400, 320
        frame.setSize(700, 620);
        //makes the window visible
        frame.setVisible(true);

    }
}
