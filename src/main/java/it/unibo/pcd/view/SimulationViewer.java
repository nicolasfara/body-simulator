package it.unibo.pcd.view;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Position;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;

public class SimulationViewer extends JFrame implements SimulatorContract.View {
    // Contract Presenter
    private SimulatorContract.Presenter mPresenter;

    private final VisualiserPanel panel;

    /**
     * Creates a view of the specified size (in pixels)
     * @param w
     * @param h
     */
    public SimulationViewer(final int w, final int h){
        super();
        setTitle("Bodies Simulation");
        setSize(w,h);
        setResizable(false);
        panel = new VisualiserPanel(w,h);
        getContentPane().add(panel);
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

    private void display(final List<Body> bodies, final double vt, final long iter){
        try {
            SwingUtilities.invokeAndWait(() -> panel.display(bodies, vt, iter));
            //SwingUtilities.invokeLater(() -> panel.display(bodies, vt, iter));
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

    private static class VisualiserPanel extends JPanel {

        private List<Body> bodies;
        private long nIter;
        private double vt;

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

            bodies.parallelStream().forEach(b -> {
                final Position p = b.getPos();
                final double rad = b.getRadius();
                final int x0 = (int)(dx + p.getX()*dx);
                final int y0 = (int)(dy - p.getY()*dy);
                g2.drawOval(x0, y0, (int)(rad*dx*2), (int)(rad*dy*2));
            });
            final String time = String.format("%.2f", vt);
            g2.drawString("Bodies: " + bodies.size() + " - vt: " + time + " - nIter: " + nIter, 2, 20);
        }

         protected void display(final List<Body> bodies, final double vt, final long iter){
            this.bodies = bodies;
            this.vt = vt;
            this.nIter = iter;
            repaint();
        }
    }
}
