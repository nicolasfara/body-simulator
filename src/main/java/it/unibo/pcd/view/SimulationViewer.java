package it.unibo.pcd.view;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Position;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SimulationViewer extends JFrame implements SimulatorContract.View {
    // Contract Presenter
    private SimulatorContract.Presenter mPresenter;

    private final VisualiserPanel panel;
    private JPanel command;
    private JButton startButton;
    private JButton stopButton;
    private JButton stepButton;

    /**
     * Creates a view of the specified size (in pixels)
     *
     * @param w
     * @param h
     */
    public SimulationViewer(final int w, final int h) {
        super();
        setTitle("Bodies Simulation");
        setSize(w, h +20);
        setResizable(false);
        panel = new VisualiserPanel(w, h );
        getContentPane().add(panel);
        this.command = new JPanel();
        this.command.setSize(w,20);
        this.startButton = new JButton("Start");
        this.stopButton = new JButton("Stop");
        this.stepButton = new JButton("Step");
        this.command.add(startButton);
        this.command.add(stopButton);
        this.command.add(stepButton);
        this.stopButton.setEnabled(false);
        this.stepButton.setEnabled(false);
        this.startButton.addActionListener(e -> {
            mPresenter.started();
            stopButton.setEnabled(true);
            startButton.setEnabled(false);
            stepButton.setEnabled(true);
        });
        this.stopButton.addActionListener(e->{
            mPresenter.stopped();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            stepButton.setEnabled(true);
        });
        this.stepButton.addActionListener(e->{
             mPresenter.step();
             startButton.setEnabled(true);
        });
        getContentPane().add(command, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent ev) {
                System.exit(-1);
            }

            @Override
            public void windowClosed(final WindowEvent ev) {
                System.exit(-1);
            }
        });
        setVisible(true);
    }

    private void display(final List<Body> bodies, final double vt, final long iter) {
        try {
            SwingUtilities.invokeAndWait(() -> panel.display(bodies, vt, iter));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateView(final List<Body> bodies, final double vt, final long iter) {
        display(bodies, vt, iter);
    }

    @Override
    public void setPresenter(final SimulatorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static class VisualiserPanel extends JPanel {

        private List<Body> bodies = new ArrayList<>();
        private long nIter;
        private double vt;


        private final long dx;
        private final long dy;

        public VisualiserPanel(final int w, final int h) {
            super();
            setSize(w, h);
            dx = w / 2 - 20;
            dy = h / 2 - 20;
        }

        @Override
        public void paint(final Graphics g) {
            final Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.clearRect(0, 0, this.getWidth(), this.getHeight());

            bodies.forEach(b -> {
                final Position p = b.getPos();
                final double rad = b.getRadius();
                final int x0 = (int) (dx + p.getX() * dx);
                final int y0 = (int) (dy - p.getY() * dy);
                g2.drawOval(x0, y0, (int) (rad * dx * 2), (int) (rad * dy * 2));
            });
            final String time = String.format("%.2f", vt);
            g2.drawString("Bodies: " + bodies.size() + " - vt: " + time + " - nIter: " + nIter, 2, 20);
        }

        public void display(final List<Body> bodies, final double vt, final long iter) {
            this.bodies = bodies;
            this.vt = vt;
            this.nIter = iter;
            repaint();
        }
    }
}
