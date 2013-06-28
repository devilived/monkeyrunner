package com.android.monkeyrunner.recorder;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Delete extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JTextField startTextField;
	private JTextField endTextField;
	private JLabel startLine;
	private JLabel endLine;
	private MonkeyRecorderFrame MRF = null;
	int startNum;
	int endNum;
	int flagEndNum;
	String sNum;
	String eNum;
	int size;
	int flagSize;

	public void actionPerformed(ActionEvent arg0) {
		sNum = startTextField.getText();
		eNum = endTextField.getText();
		size = MRF.getActionList();
		flagSize = size - 1;
		if(size == 0){
			JOptionPane.showMessageDialog(null, "没有可执行的命令！", "提示", 1);
		}else if(sNum.equals("") || eNum.equals("")){
				JOptionPane.showMessageDialog(null, "行号不能为空", "提示", 1);
		}else if(!(Pattern.matches("^[0-9]+$", sNum)
				&& Pattern.matches("^[0-9]+$", eNum))){
				JOptionPane.showMessageDialog(null, "行号只能为大于等于0的整数！", "提示", 1);
		}else{
			startNum = Integer.parseInt(sNum);
			endNum = Integer.parseInt(eNum);
			flagEndNum = endNum + 1;
			if(endNum < startNum){
//				JOptionPane.showMessageDialog(null, "结束行号应该大于开始行号！", "提示", 1);
			}else{
				if(!((endNum <= flagSize && endNum >= 0) && (startNum >= 0 && startNum <= flagSize))){
//					JOptionPane.showMessageDialog(null, "输入的行号不在可执行范围内(1~" + size
//						+ "), 请重新输入!", "提示", 1);
				}else{
					System.out.println();
//					System.out.println("您所要执行的命令区间段为：" + startNum + "~" + endNum + "行");
					this.setVisible(false);
					MRF.delete(startNum, flagEndNum);
					// Frame2 f2=new Frame2(this);
					// f2.setVisible(true);
				}
			}
		}
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(122, 92, 70, 30));
			jButton.setText("确定");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	private JLabel getStartLine() {
		if (startLine == null) {
			startLine = new JLabel();
			startLine.setBounds(new Rectangle(32, 22, 70, 25));
			startLine.setText("开始行号：");
		}
		return startLine;
	}

	private JLabel getEndLine() {
		if (endLine == null) {
			endLine = new JLabel();
			endLine.setBounds(new Rectangle(32, 52, 70, 25));
			endLine.setText("结束行号：");
		}
		return endLine;
	}

	private JTextField getStartTextField() {
		if (startTextField == null) {
			startTextField = new JTextField();
			startTextField.setBounds(new Rectangle(122, 22, 115, 25));
		}
		return startTextField;
	}

	private JTextField getEndTextField() {
		if (endTextField == null) {
			endTextField = new JTextField();
			endTextField.setBounds(new Rectangle(122, 52, 115, 25));
		}
		return endTextField;
	}

	public static void main(String[] args) {
		// TODO 自动生成方法存根

		new Delete();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Delete thisClass = new Delete();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	public Delete() {
		super();
		initialize();
		// startLine = new JLabel("开始行号：");
		// endLine = new JLabel("结束行号：");
	}

	public Delete(MonkeyRecorderFrame m) {
		this();
		MRF = m;
	}

	private void initialize() {
		this.setSize(300, 180);
		this.setContentPane(getJContentPane());
		this.setTitle("请指定要执行的命令区间段：");
		this.setResizable(false);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getStartLine());
			jContentPane.add(getEndLine());
			jContentPane.add(getStartTextField());
			jContentPane.add(getEndTextField());
			// jContentPane.add(endTextField);
			jContentPane.add(getJButton(), null);
		}
		return jContentPane;
	}

}

