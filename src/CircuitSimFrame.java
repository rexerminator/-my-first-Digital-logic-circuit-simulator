import javax.swing.JFrame;

public class CircuitSimFrame extends JFrame{
	//serialVersionUID =============================================================================
	private static final long serialVersionUID = 1L;
	//constructor ==================================================================================
	public CircuitSimFrame() {
		this.add(new CircuitSimPanel());
		this.setTitle("Circuit simulator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
