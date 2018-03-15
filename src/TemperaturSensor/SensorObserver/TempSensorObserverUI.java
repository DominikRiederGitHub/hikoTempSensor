package TemperaturSensor.SensorObserver;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Observer.ObserverListener;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.Font;

public class TempSensorObserverUI extends JFrame implements ObserverListener {

	private JFrame frame;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TempSensorObserverUI window = new TempSensorObserverUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TempSensorObserverUI() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//frame = new JFrame();
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
		scrollPane.setViewportView(textArea);
	}

	@Override
	public void updateLog(String update) {
		textArea.append(update + "\n");
	}

}
