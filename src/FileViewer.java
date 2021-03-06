import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

class FileViewer implements TreeWillExpandListener,TreeSelectionListener,ActionListener
{
 private JFrame frame = new JFrame("File Explorer");
 private Container con = null;

 private JSplitPane pMain=new JSplitPane();
 private JScrollPane pLeft=null;
 private JPanel pRight=new JPanel(new BorderLayout());
 
 private DefaultMutableTreeNode root = new DefaultMutableTreeNode("내컴퓨터");
 private JTree tree;
 
 private JPanel pNorth=new JPanel();
 private JPanel northText=new JPanel();
 private JLabel northLabel=new JLabel("");
 private JTextField pathText=new JTextField();

 private Dimension dim,dim1;
 private int xpos,ypos;
 	Panel p1 = new Panel();
 	Button bt_save = new Button("저장");
	Button bt_delete = new Button("삭제");
	Button bt_modify = new Button("복사");
 
 FileViewer(){ 
  init();
  start();
  frame.setSize(800,600);
  dim=Toolkit.getDefaultToolkit().getScreenSize();
  dim1=frame.getSize();
  xpos=(int)(dim.getWidth()/2-dim1.getWidth()/2);
  ypos=(int)(dim.getHeight()/2-dim1.getHeight()/2);
  frame.setLocation(xpos,ypos);
  frame.setVisible(true);
 
	p1.add(bt_save);
	p1.add(bt_delete);
	p1.add(bt_modify);

	 }

 void init(){
  pMain.setResizeWeight(1);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  con = frame.getContentPane();
  con.setLayout(new BorderLayout());
  
  pathText.setPreferredSize(new Dimension(500,20));
  northText.add(northLabel);
  northText.add(pathText);
  pNorth.add(northText);
  con.add(pNorth,"North");
  
  File file=new File("");
  File list[]=file.listRoots();
  DefaultMutableTreeNode temp;
  
  for(int i=0;i<list.length;++i)
  {
   temp=new DefaultMutableTreeNode(list[i].getPath());
   temp.add(new DefaultMutableTreeNode("없음"));
   root.add(temp);
  }
  tree=new JTree(root);
  pLeft=new JScrollPane(tree);
  
  pMain.setDividerLocation(150);
  pMain.setLeftComponent(pLeft);
  pMain.setRightComponent(pRight);
  con.add(pMain);
  p1.add(pRight);
 }
 	
 void start()
 {
  tree.addTreeWillExpandListener(this);
  tree.addTreeSelectionListener(this);
  	WindowListener(this);
	bt_save.addActionListener(this);
	bt_modify.addActionListener(this);
	bt_delete.addActionListener(this);
 }

 private void WindowListener(FileViewer fileViewer) {
	// TODO Auto-generated method stub
	
}

public static void main(String args[]){
  JFrame.setDefaultLookAndFeelDecorated(true);
  new FileViewer();
 }

 String getPath(TreeExpansionEvent e)
 {
  StringBuffer path=new StringBuffer();
  TreePath temp=e.getPath(); 
  Object list[]=temp.getPath();
  for(int i=0;i<list.length;++i)
  {
   if(i>0)
   {
    path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
   }
  }
  return path.toString();
 }
 String getPath(TreeSelectionEvent e)
 {
  StringBuffer path=new StringBuffer();
  TreePath temp=e.getPath(); 
  Object list[]=temp.getPath();
  for(int i=0;i<list.length;++i)
  {
   if(i>0)
   {
    path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
   }
  }
  return path.toString();
 }
 
 public void treeWillCollapse(TreeExpansionEvent event){}
 
 public void treeWillExpand(TreeExpansionEvent e)
 {
  if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("내컴퓨터")){}
  else
  {
   try{
    DefaultMutableTreeNode parent=(DefaultMutableTreeNode)e.getPath().getLastPathComponent();
    File tempFile=new File(getPath(e));
    File list[]=tempFile.listFiles();
    DefaultMutableTreeNode tempChild;
    for(File temp:list)
    {
     if(temp.isDirectory() && !temp.isHidden())
     {
      tempChild=new DefaultMutableTreeNode(temp.getName());
      if(true)
      {
       File inFile=new File(getPath(e)+temp.getName()+"\\");
       File inFileList[]=inFile.listFiles();
       for(File inTemp:inFileList)
       {
        if(inTemp.isDirectory() && !inTemp.isHidden())
        {
         tempChild.add(new DefaultMutableTreeNode("없음"));
         break;
        }
       }
      }
      parent.add(tempChild);
     }
    }
    parent.remove(0);
   }
   catch(Exception ex)
   {
    
   }
  }
 }
 public void valueChanged(TreeSelectionEvent e)
 {
  if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("내컴퓨터")){
   pathText.setText("내컴퓨터");
  }
  else
  {
   try
   {
    pathText.setText(getPath(e));
    pRight=new FView(getPath(e)).getTablePanel();
    pMain.setRightComponent(pRight);
   }
   catch(Exception ex)
   {
    JOptionPane.showMessageDialog(frame, "디스크 혹은 파일을 찾을수 없습니다.");
   }
  }
 }

@Override
public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	
}
}

class FView
{ 
 private ATable at=new ATable();
 private JTable jt=new JTable(at);
 
 JPanel pbottom=new JPanel(new BorderLayout());
 private JPanel pMain=new JPanel(new BorderLayout());
 private JScrollPane pCenter=new JScrollPane(jt);

 private File file;
 private File list[];
 private long size=0,time=0;

 FView(String str){
  init();
  start(str);
 }

 void init(){
  pMain.add(pCenter,"Center");
 }

 void start(String strPath)
 {
  file=new File(strPath);
  list=file.listFiles();
  at.setValueArr(list.length);
  for(int i=0;i<list.length;++i)
  {
   size=list[i].length();
   time=list[i].lastModified();
   for(int j=0;j<4;++j)
   {
    switch(j)
    {
     case 0:
      at.setValueAt(list[i].getName(),i,j);
      break;
     case 1:
      if(list[i].isFile())
       at.setValueAt(Long.toString((long)Math.round(size/1024.0))+"Kb",i,j);
      break;
    
     case 2:
      at.setValueAt(getFormatString(time),i,j);
      break;
    }
   }
  }
  jt.repaint();
  pCenter.setVisible(false);
  pCenter.setVisible(true);
 }

 String getLastName(String name)
 {
  int pos=name.lastIndexOf(".");
  String result=name.substring(pos+1,name.length());
  return result;
 }
 String getFormatString(long time)
 {
  SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy hh:mm a");
  Date d=new Date(time);
  String temp = sdf.format(d);
  return temp;
 }
 JPanel getTablePanel()
 {
  return pMain;
 }
}

 

class ATable extends AbstractTableModel
{
 private String title[]={"Name", "Size", "Modified"};
 private String val[][]=new String[1][3];
 
 public void setValueArr(int i)	
 {
  val=new String[i][4];
 }
 public int getRowCount()
 {
  return val.length;
 }
 public int getColumnCount()
 {
  return val[0].length;
 }
 public String getColumnName(int column )
 {
  return title[column];
 }
 public boolean isCellEditable(int rowIndex, int columnIndex)
 {
  if(columnIndex==0)
   return true;
  else
   return false;
 }
 public Object getValueAt(int row, int column)
 {
  return val[row][column];
 }
 public void setValueAt(String aValue, int rowIndex, int columnIndex ){
  val[rowIndex][columnIndex] = aValue;
 }
}