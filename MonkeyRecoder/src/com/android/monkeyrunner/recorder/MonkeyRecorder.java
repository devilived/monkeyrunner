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

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Enumeration;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Log;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyFormatter;
import com.android.monkeyrunner.MonkeyRunnerOptions;
import com.android.monkeyrunner.MonkeyRunnerStarter;

/**
 * Helper entry point for MonkeyRecorder.
 */
public class MonkeyRecorder extends JFrame implements ActionListener {
	private static final Logger LOG = Logger.getLogger(MonkeyRecorder.class
			.getName());
	// This lock is used to keep the python process blocked while the frame is
	// runing.
	private static final Object LOCK = new Object();
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private Frame f1 = null;
	static String[] margs = null;

	public void actionPerformed(ActionEvent arg0) {

		// f1.setVisible(true);
		// this.setVisible(false);
	}

	/**
	 * 
	 * 
	 * <code>
	 * from com.android.monkeyrunner import MonkeyRunner as mr
	 * from com.android.monkeyrunner import MonkeyRecorder
	 * MonkeyRecorder.start(mr.waitForConnection())
	 * </code>
	 * 
	 * @param device
	 */
	public static void start(final MonkeyDevice device) {
		start(device.getImpl());
	}

	/* package */static void start(final IChimpDevice device) {
		MonkeyRecorderFrame frame = new MonkeyRecorderFrame(device);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// TODO: this is a hack until the window listener works.
		// frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// Log.v("nan::"+this,
				// "the window is closed-----------------------");
				device.dispose();
				// System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
				// MonkeyRecorder.this.setVisible(true);
				// synchronized (LOCK) {
				// LOCK.notifyAll();
				// }
				// frame.setVisible(false);
				// MonkeyRecorder.this.setVisible(true);
			}

		});
		frame.setVisible(true);
		// synchronized (LOCK) {
		// try {
		// System.out.println("out3");
		// LOCK.wait();
		// } catch (InterruptedException e) {
		// System.out.println("out4");
		// LOG.log(Level.SEVERE, "Unexpected Exception", e);
		// }
		// System.out.println("out5");
		// }
	}

	public MonkeyRecorder() {
		super();
		initialize();
	}

	//
	// public Frame2(Frame1 f) {
	// this();
	// f1=f;
	// }

	private void initialize() {
		this.setSize(300, 180);
		this.setContentPane(getJContentPane());
		this.setTitle("MonkeyRunner");
		this.setResizable(false);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
			jContentPane.add(getJButton2(), null);
		}
		return jContentPane;
	}

	public static void main(String[] args) {
		margs = args;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MonkeyRecorder thisClass = new MonkeyRecorder();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
				int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 400) / 2;
				int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 200) / 2;
				thisClass.setLocation(w, h);
			}
		});
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(32, 22, 105, 35));
			jButton.setText("录制脚本");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ChimpChat chimp = ChimpChat.getInstance();
					JOptionPane jOptionPane = new JOptionPane();
					MonkeyRecorder.start(chimp.waitForConnection());
					// System.out.println("开始录制------------");
					// MonkeyRecorder.this.setVisible(false);
				}
			});
		}
		return jButton;
	}

	private static final void replaceAllLogFormatters(Formatter form,
			Level level) {
		LogManager mgr = LogManager.getLogManager();
		Enumeration<String> loggerNames = mgr.getLoggerNames();
		while (loggerNames.hasMoreElements()) {
			String loggerName = loggerNames.nextElement();
			Logger logger = mgr.getLogger(loggerName);
			for (Handler handler : logger.getHandlers()) {
				handler.setFormatter(form);
				handler.setLevel(level);
			}
		}
	}

	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(152, 22, 105, 35));
			jButton1.setText("运行脚本");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner f2 = new Runner(MonkeyRecorder.this);
					int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 400) / 2;
					int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 200) / 2;
					f2.setLocation(w, h);
					f2.setVisible(true);
					f2.addWindowListener(new WindowListener() {
						@Override
						public void windowOpened(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowIconified(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeiconified(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeactivated(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowClosing(WindowEvent arg0) {
							// Log.v("nan::"+this,
							// "the window is closed-----------------------windowClosing");
							MonkeyRecorder.this.setVisible(true);

						}

						@Override
						public void windowClosed(WindowEvent arg0) {
							// Log.v("nan::"+this,
							// "the window is closed-----------------------");
							// System.out.println(this+"the window is closedddddddddddddddddddddddddd");
							// TODO Auto-generated method stub

						}

						@Override
						public void windowActivated(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}
					});
					MonkeyRecorder.this.setVisible(false);

					// System.out.println("运行脚本------------");
					// AndroidDebugBridge.terminate();
					// AndroidDebugBridge bridge =
					// AndroidDebugBridge.createBridge();
					// bridge.restart();
					// System.out.println("init  is  false-------");
					// JFileChooser file = new JFileChooser(".");
					// file.setAcceptAllFileFilterUsed(false);
					// file.addChoosableFileFilter(new FileFilter() {
					//
					// @Override
					// public String getDescription() {
					// return null;
					// }
					//
					// @Override
					// public boolean accept(File f) {
					// return f.getName().endsWith("");
					// }
					// });
					// int result = file.showOpenDialog(null);
					// String path = null;
					// if (result == JFileChooser.APPROVE_OPTION) {
					// path = file.getSelectedFile().getAbsolutePath();
					// } else {
					// return;
					// }
					// if (path == null) {
					// return;
					// }
					// MonkeyRunnerOptions options = MonkeyRunnerOptions
					// .processOptions(margs, path);
					// if (options == null) {
					// return;
					// }
					// replaceAllLogFormatters(MonkeyFormatter.DEFAULT_INSTANCE,
					// options.getLogLevel());
					// MonkeyRunnerStarter runner = new MonkeyRunnerStarter(
					// options);
					// int error = runner.run();
					// System.exit(error);
				}
			});
		}
		return jButton1;
	}

	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new Rectangle(92, 95, 105, 35));
			jButton2.setText("输出结果");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Frame f2 = new Frame(MonkeyRecorder.this);
					int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 400) / 2;
					int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 200) / 2;
					f2.setLocation(w, h);
					f2.addWindowListener(new WindowListener() {
						@Override
						public void windowOpened(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowIconified(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeiconified(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeactivated(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowClosing(WindowEvent arg0) {
							// Log.v("nan::"+this,
							// "the window is closed-----------------------windowClosing");
							MonkeyRecorder.this.setVisible(true);

						}

						@Override
						public void windowClosed(WindowEvent arg0) {
							// Log.v("nan::"+this,
							// "the window is closed-----------------------");
							// System.out.println(this+"the window is closedddddddddddddddddddddddddd");
							// TODO Auto-generated method stub

						}

						@Override
						public void windowActivated(WindowEvent arg0) {
							// TODO Auto-generated method stub

						}
					});
					f2.setVisible(true);
					MonkeyRecorder.this.setVisible(false);
				}
			});
		}
		return jButton2;
	}
}
