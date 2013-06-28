/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner.recorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.recorder.actions.Action;
import com.android.monkeyrunner.recorder.actions.DragAction;
import com.android.monkeyrunner.recorder.actions.DragAction.Direction;
import com.android.monkeyrunner.recorder.actions.LongPressAction;
import com.android.monkeyrunner.recorder.actions.NewDragAction;
import com.android.monkeyrunner.recorder.actions.PaddlingAction;
import com.android.monkeyrunner.recorder.actions.PressAction;
import com.android.monkeyrunner.recorder.actions.SaveImgAction;
import com.android.monkeyrunner.recorder.actions.TouchAction;
import com.android.monkeyrunner.recorder.actions.TypeAction;
import com.android.monkeyrunner.recorder.actions.WaitAction;

/**
 * MainFrame for MonkeyRecorder.
 */
public class MonkeyRecorderFrame extends JFrame implements WindowListener {

	private static final Logger LOG = Logger
			.getLogger(MonkeyRecorderFrame.class.getName());

	private final IChimpDevice device;

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel display = null;
	private JScrollPane historyPanel = null;
	private JPanel actionPanel = null;
	private JScrollPane mPanel = null;
	private JButton waitButton = null;
	private JButton pressButton = null;
	private JButton typeButton = null;
	private JButton flingButton = null;
	private JButton exportActionButton = null;
	private JButton importActionButton = null;
	private JPopupMenu jPopupMenu = null;
	private int n = 0;
	private int startx = -1;
	private int starty = -1;
	private int endx = -1;
	private int exdy = -1;
	private int startLine;
	private int endLine;
	private int size;
	private String[] data = {"0"};
	private int num;
	private JButton imgButton = null;
	private JCheckBox paddlingButton = null;
	private JCheckBox dragButton = null;
	private JCheckBox longPressButton = null;
	private JCheckBox insterActionButton = null;
	private JButton refreshButton = null;
	private JButton play = null;
	private BufferedImage currentImage; // @jve:decl-index=0:
	private BufferedImage scaledImage = new BufferedImage(320, 480,
			BufferedImage.TYPE_INT_ARGB); // @jve:decl-index=0:

	private JList historyList;
	private JList mList;
	private ActionListModel actionListModel;
	private MListModel model1;

	private final Timer refreshTimer = new Timer(900, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			refreshDisplay(); // @jve:decl-index=0:
		}
	});

	/**
	 * This is the default constructor
	 */
	public MonkeyRecorderFrame(IChimpDevice device) {
		this.device = device;

		initialize();
	}

	private JCheckBox getInsterActionButton() {
		if (insterActionButton == null) {
			insterActionButton = new JCheckBox();
			insterActionButton.setText("Insert Action");
			insterActionButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
						}
					});
		}
		return insterActionButton;
	}

	private JButton getimportActionButton() {
		if (importActionButton == null) {
			importActionButton = new JButton();
			importActionButton.setText("Import");
			importActionButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JFileChooser fileChooser = new JFileChooser(".");
							int returnVal = fileChooser
									.showOpenDialog(MonkeyRecorderFrame.this);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								System.out.println(fileChooser
										.getSelectedFile().getAbsolutePath());// 这个就是你选择的文件夹的路径
								try {
									BufferedReader br = new BufferedReader(
											new InputStreamReader(
													new FileInputStream(
															new File(
																	fileChooser
																			.getSelectedFile()
																			.getAbsolutePath()))));
									String temp = "";
									while ((temp = br.readLine()) != null) {
										System.out.println("" + temp);
										String[] temps = temp.split("'");
//										System.out.println("" + temps.length);
//										System.out.println("" + temps[0]);
//										System.out.println("" + temps[1]);
										if (temps[0].replaceAll(" ", "")
												.equals("WAIT|{")) {

											importIndexAction(new WaitAction(
													Float.valueOf(temps[2]
															.replaceAll(":", "")
															.replaceAll(",", "")
															.replaceAll("}", ""))));
										}
										if (temps[0].replaceAll(" ", "")
												.equals("IMG|{")) {
//											System.out.println("IMG -- ");
											importIndexAction(new SaveImgAction());
											continue;
										}
										if (temps[0].replaceAll(" ", "")
												.equals("TOUCH|{")) {
											int x = Integer.parseInt(temps[2]
													.replaceAll(":", "")
													.replaceAll(",", ""));
											int y = Integer.parseInt(temps[4]
													.replaceAll(":", "")
													.replaceAll(",", ""));
											importIndexAction(new TouchAction(x,
													y, MonkeyDevice.DOWN_AND_UP));
											continue;
										}
										if (temps[0].replaceAll(" ", "")
												.equals("TYPE|{")) {
//											System.out.println("TYPE --"
//													+ temps[4]);
											importIndexAction(new TypeAction(
													temps[3]));
											continue;
										}
//										if (temps[0].replaceAll(" ", "")
//												.equals("WAIT|{")) {
//
//											importIndexAction(new WaitAction(
//													Float.valueOf(temps[2]
//															.replaceAll(":", "")
//															.replaceAll("}", "")
//															.replaceAll(",", ""))));
//										}
										if (temps[0].replaceAll(" ", "")
												.equals("PRESS|{")) {
											importIndexAction(new PressAction(
													temps[3], temps[7]));
											continue;
										}
										if (temps[0].replaceAll(" ", "")
												.equals("DRAG|{")) {
//											String[] nums = temps[2].split(",");
											String num1 = temps[2].substring(2, temps[2].length()-2);
											String[] nums = num1.split(",");
											int startx = Integer
													.parseInt(nums[0]);
//											System.out.println("startx---"+startx);
											int starty = Integer
													.parseInt(nums[1]);
//											System.out.println("starty---"+starty);
											String num2 = temps[4].substring(2, temps[4].length()-2);
											nums = num2.split(",");
//											System.out.println(nums[0]);
//											System.out.println(nums[1]);
											int endx =Integer
													.parseInt(nums[0]);
//											System.out.println("endx---*****"+endx);
											int endy = Integer
													.parseInt(nums[1]);
//											System.out.println("endy---*****"+endy);
											String duration = temps[6].replaceAll(":", "").replaceAll(",", "");
											String steps = temps[8].replaceAll(":", "").replaceAll(",", "").replaceAll("}", "");

//											System.out.println("Some DRAG --"
//													+ temps[1]);
//											System.out.println("startx ="
//													+ startx + " starty ="
//													+ starty + " endx =" + endx
//													+ " endy =" + endy
//													+ " duration =" + duration
//													+ " step =" + steps);
											if (startx == endx
													&& starty == endy) {
//												System.out
//														.println("test-----------------------");
												importIndexAction(new LongPressAction(
														startx, starty));
												continue;
											}
											if (duration.equals("4.0")
													&& steps.equals("10")) {
												importIndexAction(new NewDragAction(
														startx, starty, endx,
														endy));
												continue;
											}
											if (duration.equals("1.0")
													&& steps.equals("100")) {
												importIndexAction(new PaddlingAction(
														startx, starty, endx,
														endy));
												continue;
											}
										}
									}
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException a) {
									// TODO Auto-generated catch block
									a.printStackTrace();
								}
							}

						}
					});
		}
		return importActionButton;
	}

	private JButton getPlayButton() {
		if (play == null) {
			play = new JButton();
			play.setText("Play");
			play.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Play play = new Play(MonkeyRecorderFrame.this);
					int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 400) / 2;
					int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 200) / 2;
					play.setLocation(w, h);
					play.setVisible(true);
					
//					JOptionPane.showMessageDialog(null,
//							"不存在mr文件 ! ");
//					JOptionPane.showInputDialog(null, "ddddd");
//					String line = JOptionPane.showInputDialog("请输入开始与结束行号！");
//					System.out.println("line:--------" + line);
//					String[] line2 = line.split(",");
					
					
					
//					int i = 0, j = 0;
//					i = Integer.parseInt(line2[0]);
//					System.out.println(i);
//					j = Integer.parseInt(line2[1]);
//					System.out.println(j);
					int num = 0;
//					for (Action mAction : actionListModel.getActionList().subList(startLine, endLine)) {
//						
//						num ++;
//						System.out.println("执行命令的行数：" + num);
//						System.out.println("size:--------");
//						System.out.println("size:" + mAction.getDisplayName() + "");
//						try {
//							mAction.execute(device);
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
				}
			});
		}
		return play;
	}
	
	public void playScript(int startLine, int endLine){
		for (Action mAction : actionListModel.getActionList().subList(startLine, endLine)) {
//			System.out.println(startLine);
//			System.out.println(endLine);
//			System.out.println("Command:" + mAction.getDisplayName() + "");
			try {
				mAction.execute(device);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public int getActionList(){
		size = actionListModel.getActionList().size();
		return size;
	}

	private JCheckBox getPaddlingButton() {
		if (paddlingButton == null) {
			paddlingButton = new JCheckBox();
			paddlingButton.setText("Paddling");
			paddlingButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (paddlingButton.isSelected()) {
								n = 0;
								longPressButton.setSelected(false);
								dragButton.setSelected(false);
							}

						}
					});
		}
		return paddlingButton;
	}

	private JCheckBox getLongPressButton() {
		if (longPressButton == null) {
			longPressButton = new JCheckBox();
			longPressButton.setText("Long Press");
			longPressButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (longPressButton.isSelected()) {
								n = 0;
								paddlingButton.setSelected(false);
								dragButton.setSelected(false);
							}
						}
					});
		}
		return longPressButton;
	}

	private JCheckBox getDragButton() {
		if (dragButton == null) {
			dragButton = new JCheckBox();
			dragButton.setText("Drag");
			dragButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (dragButton.isSelected()) {
						n = 0;
						paddlingButton.setSelected(false);
						longPressButton.setSelected(false);
					}
				}
			});
		}
		return dragButton;
	}

	private JButton getimgButton() {
		if (imgButton == null) {
			imgButton = new JButton();
			imgButton.setText("Save Img");
			imgButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addIndexAction(new SaveImgAction());
				}
			});
		}
		return imgButton;
	}

	private void initialize() {
		this.setSize(400, 600);
		this.setContentPane(getJContentPane());
		this.setTitle("MonkeyRecorder");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refreshDisplay();
			}
		});

		refreshTimer.start();

	}

	private void refreshDisplay() {
		IChimpImage snapshot = device.takeSnapshot();
		currentImage = snapshot.createBufferedImage();
		Graphics2D g = scaledImage.createGraphics();
		g.drawImage(currentImage, 0, 0, scaledImage.getWidth(),
				scaledImage.getHeight(), null);
		g.dispose();
		display.setIcon(new ImageIcon(scaledImage));
		pack();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			display = new JLabel();
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(display, BorderLayout.WEST);
			jContentPane.add(getHistoryPanel(), BorderLayout.CENTER);
//			jContentPane.add(getmPanel(),BorderLayout.EAST);
			jContentPane.add(getActionPanel(), BorderLayout.NORTH);
//			mPanel.setVisible(true);
			display.setPreferredSize(new Dimension(320, 480));

			display.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					touch(event);
				}
			});
		}
		return jContentPane;
	}

	/**
	 * This method initializes historyPanel
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getHistoryPanel() {
		if (historyPanel == null) {
			historyPanel = new JScrollPane();
			historyPanel.getViewport().setView(getHistoryList());
		}
		return historyPanel;
	}
	private JScrollPane getmPanel() {
		if (mPanel == null) {
			mPanel = new JScrollPane();
//			mPanel.setBounds(r)
			mPanel.getViewport().setView(getmList());
		}
		
		return mPanel;
	}
	private JList getmList() {


		if (mList == null) {
			model1 = new MListModel();
//			System.out.println(historyList.getModel().getSize());
			num = historyList.getModel().getSize();
			
	
			

//				model.addElement(i);
//				model1 = new ActionListModel();
		//	data[i].equals(i+"");
			mList = new JList(model1);
//			System.out.println(model1);
		
			if(mList != null){
			mList.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
//					System.out.println(mList.getSelectedIndex()+"ssss");
//					System.out.println(model1.getElementAt(historyList.getSelectedIndex()));
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
			});
			
			}
		}
		
		return mList;
		
	}
	private JList getHistoryList() {
		if (jPopupMenu == null) {
			jPopupMenu = new JPopupMenu();
			JMenuItem mjmenuitemItem1 = new JMenuItem("line number");
			JMenuItem mjmenuitemItem = new JMenuItem("del");
			JMenuItem delete = new JMenuItem("del all");
		//	JMenuItem mjmenuitemItem1 = new JMenuItem("num ");
			mjmenuitemItem1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null,
							"第"+(historyList.getSelectedIndex()+1)+"行"	);
				}
				});
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Delete del = new Delete(MonkeyRecorderFrame.this);
					int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 400) / 2;
					int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 200) / 2;
					del.setLocation(w, h);
					del.setVisible(true);
				}
				});
			mjmenuitemItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actionListModel.remove(historyList.getSelectedIndex());
					int i = 0;
					if (num != historyList.getModel().getSize() ) {
						model1.remove(num-1);
						
						//actionListModel = new ActionListModel();
						num = historyList.getModel().getSize();
//						System.out.println(historyList.getModel().getSize());
						for(i = 0;i<historyList.getModel().getSize();i++){
						
					    data=Arrays.copyOf(data, data.length+1);
					    data[data.length-1]=i+"";
					   
					//	data[i].equals(i+"");
						mList = new JList(model1);
					//    model.addElement(i);
					//	System.out.println(data);
						
						}
					}
				}
			});
			jPopupMenu.add(mjmenuitemItem);
			jPopupMenu.add(mjmenuitemItem1);
			jPopupMenu.add(delete);
		}
		if (historyList == null) {
			actionListModel = new ActionListModel();
			historyList = new JList(actionListModel);
			historyList.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
//					System.out.println(historyList.getSelectedIndex()+"sssssssssss");
					
				
					if (e.getButton() == 3
							&& historyList.getSelectedIndex() >= 0) {
						jPopupMenu.show(historyList, e.getX(), e.getY());
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
			});
		}
		return historyList;
	}

	/**
	 * This method initializes actionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getActionPanel() {
		if (actionPanel == null) {
			actionPanel = new JPanel();
			actionPanel.setLayout(new BoxLayout(getActionPanel(),
					BoxLayout.X_AXIS));
			// duxuebin start
			actionPanel.add(getInsterActionButton(), null);
			actionPanel.add(getPaddlingButton(), null);
			actionPanel.add(getDragButton(), null);
			actionPanel.add(getLongPressButton(), null);
			actionPanel.add(getPlayButton(), null);
			actionPanel.add(getimgButton(), null);
			// duxuebin end
			actionPanel.add(getWaitButton(), null);
			actionPanel.add(getPressButton(), null);
			actionPanel.add(getTypeButton(), null);
			// actionPanel.add(getFlingButton(), null);
			actionPanel.add(getExportActionButton(), null);
			actionPanel.add(getimportActionButton(), null);
			actionPanel.add(getRefreshButton(), null);
		}
		return actionPanel;
	}

	/**
	 * This method initializes waitButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getWaitButton() {
		if (waitButton == null) {
			waitButton = new JButton();
			waitButton.setText("Wait");
			waitButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String howLongStr = JOptionPane
							.showInputDialog("How many seconds to wait?");
					if (howLongStr != null) {
						float howLong = Float.parseFloat(howLongStr);
						addIndexAction(new WaitAction(howLong));
					}
				}
			});
		}
		return waitButton;
	}

	/**
	 * This method initializes pressButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPressButton() {
		if (pressButton == null) {
			pressButton = new JButton();
			pressButton.setText("Press a Button");
			pressButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JPanel panel = new JPanel();
					JLabel text = new JLabel("What button to press?");
					JComboBox keys = new JComboBox(PressAction.KEYS);
					keys.setEditable(true);
					JComboBox direction = new JComboBox(
							PressAction.DOWNUP_FLAG_MAP.values().toArray());
					panel.add(text);
					panel.add(keys);
					panel.add(direction);

					int result = JOptionPane.showConfirmDialog(null, panel,
							"Input", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						// Look up the "flag" value for the press choice
						Map<String, String> lookupMap = PressAction.DOWNUP_FLAG_MAP
								.inverse();
						String flag = lookupMap.get(direction.getSelectedItem());
						addIndexAction(new PressAction((String) keys
								.getSelectedItem(), flag));
					}
				}
			});
		}
		return pressButton;
	}

	/**
	 * This method initializes typeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getTypeButton() {
		if (typeButton == null) {
			typeButton = new JButton();
			typeButton.setText("Type Something");
			typeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String whatToType = JOptionPane
							.showInputDialog("What to type?");
					if (whatToType != null) {
						addIndexAction(new TypeAction(whatToType));
					}
				}
			});
		}
		return typeButton;
	}

	/**
	 * This method initializes flingButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getFlingButton() {
		if (flingButton == null) {
			flingButton = new JButton();
			flingButton.setText("Fling");
			flingButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					panel.add(new JLabel("Which Direction to fling?"));
					JComboBox directionChooser = new JComboBox(
							DragAction.Direction.getNames());
					panel.add(directionChooser);
					panel.add(new JLabel("How long to drag (in ms)?"));
					JTextField ms = new JTextField();
					ms.setText("1000");
					panel.add(ms);
					panel.add(new JLabel("How many steps to do it in?"));
					JTextField steps = new JTextField();
					steps.setText("10");
					panel.add(steps);

					int result = JOptionPane.showConfirmDialog(null, panel,
							"Input", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						DragAction.Direction dir = DragAction.Direction
								.valueOf((String) directionChooser
										.getSelectedItem());
						long millis = Long.parseLong(ms.getText());
						int numSteps = Integer.parseInt(steps.getText());

						addIndexAction(newFlingAction(dir, numSteps, millis));
					}
				}
			});
		}
		return flingButton;
	}

	private DragAction newFlingAction(Direction dir, int numSteps, long millis) {
		int width = Integer.parseInt(device.getProperty("display.width"));
		int height = Integer.parseInt(device.getProperty("display.height"));

		// Adjust the w/h to a pct of the total size, so we don't hit things on
		// the "outside"
		width = (int) (width * 0.8f);
		height = (int) (height * 0.8f);
		int minW = (int) (width * 0.2f);
		int minH = (int) (height * 0.2f);

		int midWidth = width / 2;
		int midHeight = height / 2;

		int startx = minW;
		int starty = minH;
		int endx = minW;
		int endy = minH;

		switch (dir) {
		case NORTH:
			startx = endx = midWidth;
			starty = height;
			break;
		case SOUTH:
			startx = endx = midWidth;
			endy = height;
			break;
		case EAST:
			starty = endy = midHeight;
			endx = width;
			break;
		case WEST:
			starty = endy = midHeight;
			startx = width;
			break;
		}

		return new DragAction(dir, startx, starty, endx, endy, numSteps, millis);
	}

	/**
	 * This method initializes exportActionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportActionButton() {
		if (exportActionButton == null) {
			exportActionButton = new JButton();
			exportActionButton.setText("Export Actions");
			exportActionButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(
								java.awt.event.ActionEvent ev) {
							JFileChooser fc = new JFileChooser();
							if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
								try {
									actionListModel.export(fc.getSelectedFile());
								} catch (FileNotFoundException e) {
									LOG.log(Level.SEVERE,
											"Unable to save file", e);
								}
							}
						}
					});
		}
		return exportActionButton;
	}

	/**
	 * This method initializes refreshButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = new JButton();
			refreshButton.setText("Refresh Display");
			refreshButton
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							refreshDisplay();
						}
					});
		}
		return refreshButton;
	}

	private void touch(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();

		// Since we scaled the image down, our x/y are scaled as well.
		double scalex = ((double) currentImage.getWidth())
				/ ((double) scaledImage.getWidth());
		double scaley = ((double) currentImage.getHeight())
				/ ((double) scaledImage.getHeight());

		x = (int) (x * scalex);
		y = (int) (y * scaley);

		switch (event.getID()) {
		case MouseEvent.MOUSE_CLICKED:
			if (paddlingButton.isSelected()) {
				switch (++n) {
				case 1:
					startx = x;
					starty = y;
					break;
				case 2:
					endx = x;
					exdy = y;
					paddlingButton.setSelected(false);
					addIndexAction(new PaddlingAction(startx, starty, endx,
							exdy));
					break;
				default:
					n = 0;
					break;
				}
			} else if (dragButton.isSelected()) {
				switch (++n) {
				case 1:
					startx = x;
					starty = y;
					break;
				case 2:
					endx = x;
					exdy = y;
					dragButton.setSelected(false);
					addIndexAction(new NewDragAction(startx, starty, endx, exdy));
					break;
				default:
					n = 0;
					break;
				}
			} else if (longPressButton.isSelected()) {
				longPressButton.setSelected(false);
				addIndexAction(new LongPressAction(x, y));
			} else {
				addIndexAction(new TouchAction(x, y, MonkeyDevice.DOWN_AND_UP));
			}

			break;
		case MouseEvent.MOUSE_PRESSED:
			addIndexAction(new TouchAction(x, y, MonkeyDevice.DOWN));
			break;
		case MouseEvent.MOUSE_RELEASED:
			addIndexAction(new TouchAction(x, y, MonkeyDevice.UP));
			break;
		}
	}
	private void importIndexAction(Action a) {
		int i = 0;
		actionListModel.add(a);
		if (num != historyList.getModel().getSize() ) {
			//actionListModel = new ActionListModel();
			num = historyList.getModel().getSize();
//			System.out.println(historyList.getModel().getSize());
			for(i = 0;i<historyList.getModel().getSize();i++){
			
		    data=Arrays.copyOf(data, data.length+1);
		    data[data.length-1]=i+"";
			
			data[i].equals(i+"");
			mList = new JList(model1);
			
		//	model.addElement(i);
			
//			System.out.println(data+"jjjjjjjjjjjjjjjjjjjjjjjj");
			}
			model1.newAdd(i+"    ");
			if(mList != null){
				mList.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
//						System.out.println(mList.getSelectedIndex()+"222222222222222");
						
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
					}
				});
					
			}
		}
		
	}
	public void addIndexAction(Action a) {

		if (insterActionButton.isSelected()
				&& historyList.getSelectedIndex() >= 0) {
//			System.out.println("add indexAction");
			actionListModel.addActionItem(historyList.getSelectedIndex(), a);
			try {
				a.execute(device);
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Unable to execute action!", e);
			}
			insterActionButton.setSelected(false);
		} else {
//			System.out.println("add Action");
			addAction(a);
		}
//		reAdd();
//		if (num != historyList.getModel().getSize() ) {
//
//		
//			//actionListModel = new ActionListModel();
//			num = historyList.getModel().getSize();
//			System.out.println(historyList.getModel().getSize());
//			for(i = 0;i<historyList.getModel().getSize();i++){
//			
//		    data=Arrays.copyOf(data, data.length+1);
//		    data[data.length-1]=i+"";
//		   
//		//	data[i].equals(i+"");
//			mList = new JList(model1);
//		//    model.addElement(i);
//		//	System.out.println(data);
//			
//			}
//			 model1.newAdd(i+"   ");
//			if(mList != null){
//				mList.addMouseListener(new MouseListener() {
//					@Override
//					public void mouseClicked(MouseEvent e) {
//						System.out.println(mList.getSelectedIndex()+"ddddd");
////						model.
//					}
//
//					@Override
//					public void mouseEntered(MouseEvent arg0) {
//					}
//
//					@Override
//					public void mouseExited(MouseEvent arg0) {
//					}
//
//					@Override
//					public void mousePressed(MouseEvent arg0) {
//					}
//
//					@Override
//					public void mouseReleased(MouseEvent arg0) {
//					}
//				});
//					
//			}
//		}
	
	}
//	public void reAdd(){
//		int i = 0;
//		if (num != historyList.getModel().getSize() ) {
//
//			
//			//actionListModel = new ActionListModel();
//			num = historyList.getModel().getSize();
//			System.out.println(historyList.getModel().getSize());
//			for(i = 0;i<historyList.getModel().getSize();i++){
//			
//		    data=Arrays.copyOf(data, data.length+1);
//		    data[data.length-1]=i+"";
//		   
//		//	data[i].equals(i+"");
//			mList = new JList(model1);
//		//    model.addElement(i);
//		//	System.out.println(data);
//			
//			}
//			 model1.newAdd(i+"   ");
//			if(mList != null){
//				mList.addMouseListener(new MouseListener() {
//					@Override
//					public void mouseClicked(MouseEvent e) {
//						System.out.println(mList.getSelectedIndex()+"ddddd");
////						model.
//					}
//
//					@Override
//					public void mouseEntered(MouseEvent arg0) {
//					}
//
//					@Override
//					public void mouseExited(MouseEvent arg0) {
//					}
//
//					@Override
//					public void mousePressed(MouseEvent arg0) {
//					}
//
//					@Override
//					public void mouseReleased(MouseEvent arg0) {
//					}
//				});
//					
//			}
//		}
//	}

	public void addAction(Action a) {

		actionListModel.add(a);
		try {
			a.execute(device);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unable to execute action!", e);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		device.dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		device.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		device.dispose();
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		device.dispose();
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
	public void delete(int start,int end){
		for(int i = start;i<end;i++){
			actionListModel.remove(i);
			int j = 0;
			if (num != historyList.getModel().getSize() ) {
//				System.out.println(num+",,,,...,,,...");
//				System.out.println((num-1));
				model1.remove((num-1));
				
				//actionListModel = new ActionListModel();
				num = historyList.getModel().getSize();
//				System.out.println(historyList.getModel().getSize());

			}
		}
	}
}
