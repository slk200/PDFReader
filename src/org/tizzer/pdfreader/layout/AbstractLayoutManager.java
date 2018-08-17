package org.tizzer.pdfreader.layout;

import java.awt.*;

public abstract class AbstractLayoutManager implements LayoutManager2 {
    public AbstractLayoutManager() {
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        this.addComponent(comp, constraints);
    }

    public void addLayoutComponent(String name, Component comp) {
        this.addComponent(comp, name);
    }

    public void removeLayoutComponent(Component comp) {
        this.removeComponent(comp);
    }

    public void addComponent(Component component, Object constraints) {
    }

    public void removeComponent(Component component) {
    }

    public Dimension minimumLayoutSize(Container parent) {
        return this.preferredLayoutSize(parent);
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.5F;
    }

    public void invalidateLayout(Container target) {
    }
}
