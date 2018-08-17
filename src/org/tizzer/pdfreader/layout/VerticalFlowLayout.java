package org.tizzer.pdfreader.layout;

import java.awt.*;

public class VerticalFlowLayout extends AbstractLayoutManager {
    public static final int TOP = 0;
    public static final int MIDDLE = 1;
    public static final int BOTTOM = 2;
    protected int align;
    protected int hgap;
    protected int vgap;
    protected boolean hfill;
    protected boolean vfill;

    public VerticalFlowLayout() {
        this(0, 0, 0, true, false);
    }

    public VerticalFlowLayout(boolean hfill, boolean vfill) {
        this(0, 0, 0, hfill, vfill);
    }

    public VerticalFlowLayout(int hgap, int vgap) {
        this(0, hgap, vgap, true, false);
    }

    public VerticalFlowLayout(int align, int hgap, int vgap) {
        this(align, hgap, vgap, true, false);
    }

    public VerticalFlowLayout(int align) {
        this(align, 0, 0, true, false);
    }

    public VerticalFlowLayout(int align, boolean hfill, boolean vfill) {
        this(align, 0, 0, hfill, vfill);
    }

    public VerticalFlowLayout(int hgap, int vgap, boolean hfill, boolean vfill) {
        this(0, hgap, vgap, hfill, vfill);
    }

    public VerticalFlowLayout(int align, int hgap, int vgap, boolean hfill, boolean vfill) {
        this.align = align;
        this.hgap = hgap;
        this.vgap = vgap;
        this.hfill = hfill;
        this.vfill = vfill;
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    public boolean getVerticalFill() {
        return this.vfill;
    }

    public void setVerticalFill(boolean vfill) {
        this.vfill = vfill;
    }

    public boolean getHorizontalFill() {
        return this.hfill;
    }

    public void setHorizontalFill(boolean hfill) {
        this.hfill = hfill;
    }

    public Dimension preferredLayoutSize(Container target) {
        Dimension tarsiz = new Dimension(0, 0);

        for (int i = 0; i < target.getComponentCount(); ++i) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                tarsiz.width = Math.max(tarsiz.width, d.width);
                if (i > 0) {
                    tarsiz.height += this.vgap;
                }

                tarsiz.height += d.height;
            }
        }

        Insets insets = target.getInsets();
        tarsiz.width += insets.left + insets.right;
        tarsiz.height += insets.top + insets.bottom;
        return tarsiz;
    }

    public Dimension minimumLayoutSize(Container target) {
        Dimension tarsiz = new Dimension(0, 0);

        for (int i = 0; i < target.getComponentCount(); ++i) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getMinimumSize();
                tarsiz.width = Math.max(tarsiz.width, d.width);
                if (i > 0) {
                    tarsiz.height += this.vgap;
                }

                tarsiz.height += d.height;
            }
        }

        Insets insets = target.getInsets();
        tarsiz.width += insets.left + insets.right;
        tarsiz.height += insets.top + insets.bottom;
        return tarsiz;
    }

    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        Dimension size = target.getSize();
        int maxwidth = size.width - (insets.left + insets.right);
        int maxheight = size.height - (insets.top + insets.bottom);
        int numcomp = target.getComponentCount();
        int pheight = !this.vfill && this.align != 0 ? this.calculatePreferredHeight(target) : 0;
        int y = 0;

        for (int i = 0; i < numcomp; ++i) {
            Component component = target.getComponent(i);
            if (component.isVisible()) {
                Dimension ps = component.getPreferredSize();
                int w = this.hfill ? maxwidth : Math.min(maxwidth, ps.width);
                int h = this.vfill && i == numcomp - 1 ? maxheight - y : ps.height;
                if (this.vfill) {
                    component.setBounds(insets.left, insets.top + y, w, h);
                } else {
                    switch (this.align) {
                        case 1:
                            component.setBounds(insets.left, insets.top + maxheight / 2 - pheight / 2 + y, w, ps.height);
                            break;
                        case 2:
                            component.setBounds(insets.left, size.height - insets.bottom - pheight + y, w, ps.height);
                            break;
                        default:
                            component.setBounds(insets.left, insets.top + y, w, ps.height);
                    }
                }

                y += h + this.vgap;
            }
        }

    }

    protected int calculatePreferredHeight(Container target) {
        int ph = 0;

        for (int i = 0; i < target.getComponentCount(); ++i) {
            ph += target.getComponent(i).getPreferredSize().height;
            if (i < target.getComponentCount() - 1) {
                ph += this.vgap;
            }
        }

        return ph;
    }
}
