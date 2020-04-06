package it.unibo.pcd.view;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Position;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;

public class SimulationViewer extends JFrame implements SimulatorContract.View {
    // Contract Presenter
    private SimulatorContract.Presenter mPresenter;

    private final VisualiserPanel panel;

    private final JButton stopButton =  new JButton("Stop");
    private final JButton startButton = new JButton("Start");
    private final JButton stepButton = new JButton("Step");

    private final JLabel bodiesLabel = new JLabel("Bodies: 0");
    private final JLabel vtLabel = new JLabel("vt: 0");
    private final JLabel iterLabel = new JLabel("iteration: 0");

    /**
     * Creates a view of the specified size (in pixels)
     * @param w Width of simulation panel.
     * @param h Height of simulation panel.
     */
    public SimulationViewer(final int w, final int h){
        super();
        setTitle("Bodies Simulation");
        setSize(w,h);
        setResizable(false);

        JPanel buttonsLayout = new JPanel();
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonsLayout.setLayout(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(bodiesLabel);
        infoPanel.add(vtLabel);
        infoPanel.add(iterLabel);
        buttonsLayout.add(infoPanel);
        buttonsLayout.add(stopButton);
        buttonsLayout.add(startButton);
        buttonsLayout.add(stepButton);
        panel = new VisualiserPanel(w,h);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonsLayout, BorderLayout.NORTH);

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        stopButton.addActionListener(btn -> {
            mPresenter.stopSimulation();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            stepButton.setEnabled(true);
        });

        startButton.addActionListener(act -> {
            mPresenter.startSimulation();
            stopButton.setEnabled(true);
            startButton.setEnabled(false);
            stepButton.setEnabled(false);
        });

        stepButton.addActionListener(act -> {
            mPresenter.step();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(final WindowEvent ev){
                Runtime.getRuntime().exit(0);
            }
            @Override
            public void windowClosed(final WindowEvent ev){
                Runtime.getRuntime().exit(0);
            }
        });
        setVisible(true);
    }

    private void display(final List<Body> bodies){
        try {
            //SwingUtilities.invokeAndWait(() -> panel.display(bodies, vt, iter));
            SwingUtilities.invokeLater(() -> panel.display(bodies));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayVt(final double vt) {
        SwingUtilities.invokeLater(() -> vtLabel.setText("vt: " + String.format("%.2f", vt)));
    }

    private void displayIter(final long iter) {
        SwingUtilities.invokeLater(() -> iterLabel.setText("Iteration: " + iter));
    }

    @Override
    public void setPresenter(final SimulatorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateBodies(List<Body> bodies) {
        bodiesLabel.setText("Bodies: " + bodies.size());
        display(bodies);
    }

    @Override
    public void updateVt(double vt) {
        displayVt(vt);
    }

    @Override
    public void updateIter(long iter) {
        displayIter(iter);
    }

    private static class VisualiserPanel extends JPanel {

        private List<Body> bodies;

        private final long dx;
        private final long dy;

        VisualiserPanel(final int w, final int h){
            super();
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
        }

        @Override
        public void paint(final Graphics g){
            final Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.clearRect(0,0,this.getWidth(),this.getHeight());

            if (bodies != null) {
                bodies.forEach(b -> {
                    final Position p = b.getPos();
                    final double rad = b.getRadius();
                    final int x0 = (int) (dx + p.getX() * dx);
                    final int y0 = (int) (dy - p.getY() * dy);
                    g2.drawOval(x0, y0, (int) (rad * dx * 2), (int) (rad * dy * 2));
                });
            }
        }

         protected void display(final List<Body> bodies){
            this.bodies = bodies;
            repaint();
        }
    }
}
