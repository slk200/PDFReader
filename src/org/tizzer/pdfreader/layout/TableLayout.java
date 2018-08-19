package org.tizzer.pdfreader.layout;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TableLayout implements LayoutManager2, Serializable, TableLayoutConstants {
    protected static final double[][] defaultSize = new double[][]{new double[0], new double[0]};
    protected static final int C = 0;
    protected static final int R = 1;
    protected static boolean checkForComponentOrientationSupport = true;
    protected static Method methodGetComponentOrientation;
    protected double[][] crSpec;
    protected int[][] crSize;
    protected int[][] crOffset;
    protected LinkedList list;
    protected boolean dirty;
    protected int oldWidth;
    protected int oldHeight;
    protected int hGap;
    protected int vGap;

    public TableLayout() {
        this.crSpec = new double[][]{null, null};
        this.crSize = new int[][]{null, null};
        this.crOffset = new int[][]{null, null};
        this.init(defaultSize[0], defaultSize[1]);
    }

    public TableLayout(int hGap, int vGap) {
        this.crSpec = new double[][]{null, null};
        this.crSize = new int[][]{null, null};
        this.crOffset = new int[][]{null, null};
        this.init(defaultSize[0], defaultSize[1]);
        this.setGaps(hGap, vGap);
    }

    public TableLayout(double[][] size) {
        this.crSpec = new double[][]{null, null};
        this.crSize = new int[][]{null, null};
        this.crOffset = new int[][]{null, null};
        if (size != null && size.length == 2) {
            this.init(size[0], size[1]);
        } else {
            throw new IllegalArgumentException("Parameter size should be an array, a[2], where a[0] is the is an array of column widths and a[1] is an array or row heights.");
        }
    }

    public TableLayout(double[][] size, int hGap, int vGap) {
        this(size);
        this.setGaps(hGap, vGap);
    }

    public TableLayout(double[] col, double[] row) {
        this.crSpec = new double[][]{null, null};
        this.crSize = new int[][]{null, null};
        this.crOffset = new int[][]{null, null};
        this.init(col, row);
    }

    public TableLayout(double[] col, double[] row, int hGap, int vGap) {
        this(col, row);
        this.setGaps(hGap, vGap);
    }

    protected void init(double[] col, double[] row) {
        if (col == null) {
            throw new IllegalArgumentException("Parameter col cannot be null");
        } else if (row == null) {
            throw new IllegalArgumentException("Parameter row cannot be null");
        } else {
            this.crSpec[0] = new double[col.length];
            this.crSpec[1] = new double[row.length];
            System.arraycopy(col, 0, this.crSpec[0], 0, this.crSpec[0].length);
            System.arraycopy(row, 0, this.crSpec[1], 0, this.crSpec[1].length);

            int counter;
            for (counter = 0; counter < this.crSpec[0].length; ++counter) {
                if (this.crSpec[0][counter] < 0.0D && this.crSpec[0][counter] != -1.0D && this.crSpec[0][counter] != -2.0D && this.crSpec[0][counter] != -3.0D) {
                    this.crSpec[0][counter] = 0.0D;
                }
            }

            for (counter = 0; counter < this.crSpec[1].length; ++counter) {
                if (this.crSpec[1][counter] < 0.0D && this.crSpec[1][counter] != -1.0D && this.crSpec[1][counter] != -2.0D && this.crSpec[1][counter] != -3.0D) {
                    this.crSpec[1][counter] = 0.0D;
                }
            }

            this.list = new LinkedList();
            this.dirty = true;
        }
    }

    public TableLayoutConstraints getConstraints(Component component) {
        ListIterator iterator = this.list.listIterator(0);

        TableLayout.Entry entry;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entry = (TableLayout.Entry) iterator.next();
        } while (entry.component != component);

        return new TableLayoutConstraints(entry.cr1[0], entry.cr1[1], entry.cr2[0], entry.cr2[1], entry.alignment[0], entry.alignment[1]);
    }

    public void setConstraints(Component component, TableLayoutConstraints constraint) {
        if (component == null) {
            throw new IllegalArgumentException("Parameter component cannot be null.");
        } else if (constraint == null) {
            throw new IllegalArgumentException("Parameter constraint cannot be null.");
        } else {
            ListIterator iterator = this.list.listIterator(0);

            while (iterator.hasNext()) {
                TableLayout.Entry entry = (TableLayout.Entry) iterator.next();
                if (entry.component == component) {
                    iterator.set(new TableLayout.Entry(component, constraint));
                }
            }

        }
    }

    protected void setCr(int z, double[] size) {
        this.crSpec[z] = new double[size.length];
        System.arraycopy(size, 0, this.crSpec[z], 0, this.crSpec[z].length);

        for (int counter = 0; counter < this.crSpec[z].length; ++counter) {
            if (this.crSpec[z][counter] < 0.0D && this.crSpec[z][counter] != -1.0D && this.crSpec[z][counter] != -2.0D && this.crSpec[z][counter] != -3.0D) {
                this.crSpec[z][counter] = 0.0D;
            }
        }

        this.dirty = true;
    }

    public void setColumn(int i, double size) {
        this.setCr(0, i, size);
    }

    public void setRow(int i, double size) {
        this.setCr(1, i, size);
    }

    protected void setCr(int z, int i, double size) {
        if (size < 0.0D && size != -1.0D && size != -2.0D && size != -3.0D) {
            size = 0.0D;
        }

        this.crSpec[z][i] = size;
        this.dirty = true;
    }

    public double[] getColumn() {
        double[] column = new double[this.crSpec[0].length];
        System.arraycopy(this.crSpec[0], 0, column, 0, column.length);
        return column;
    }

    public void setColumn(double[] column) {
        this.setCr(0, column);
    }

    public double[] getRow() {
        double[] row = new double[this.crSpec[1].length];
        System.arraycopy(this.crSpec[1], 0, row, 0, row.length);
        return row;
    }

    public void setRow(double[] row) {
        this.setCr(1, row);
    }

    public double getColumn(int i) {
        return this.crSpec[0][i];
    }

    public double getRow(int i) {
        return this.crSpec[1][i];
    }

    public int getNumColumn() {
        return this.crSpec[0].length;
    }

    public int getNumRow() {
        return this.crSpec[1].length;
    }

    public int getHGap() {
        return this.hGap;
    }

    public void setHGap(int hGap) {
        if (hGap >= 0) {
            this.hGap = hGap;
        } else {
            throw new IllegalArgumentException("Parameter hGap must be non-negative.");
        }
    }

    public int getVGap() {
        return this.vGap;
    }

    public void setVGap(int vGap) {
        if (vGap >= 0) {
            this.vGap = vGap;
        } else {
            throw new IllegalArgumentException("Parameter vGap must be non-negative.");
        }
    }

    public void setGaps(int hGap, int vGap) {
        this.setHGap(hGap);
        this.setVGap(vGap);
    }

    public void insertColumn(int i, double size) {
        this.insertCr(0, i, size);
    }

    public void insertRow(int i, double size) {
        this.insertCr(1, i, size);
    }

    public void insertCr(int z, int i, double size) {
        if (i >= 0 && i <= this.crSpec[z].length) {
            if (size < 0.0D && size != -1.0D && size != -2.0D && size != -3.0D) {
                size = 0.0D;
            }

            double[] cr = new double[this.crSpec[z].length + 1];
            System.arraycopy(this.crSpec[z], 0, cr, 0, i);
            System.arraycopy(this.crSpec[z], i, cr, i + 1, this.crSpec[z].length - i);
            cr[i] = size;
            this.crSpec[z] = cr;
            ListIterator iterator = this.list.listIterator(0);

            while (iterator.hasNext()) {
                TableLayout.Entry entry = (TableLayout.Entry) iterator.next();
                if (entry.cr1[z] >= i) {
                    ++entry.cr1[z];
                }

                if (entry.cr2[z] >= i) {
                    ++entry.cr2[z];
                }
            }

            this.dirty = true;
        } else {
            throw new IllegalArgumentException("Parameter i is invalid.  i = " + i + ".  Valid range is [0, " + this.crSpec[z].length + "].");
        }
    }

    public void deleteColumn(int i) {
        this.deleteCr(0, i);
    }

    public void deleteRow(int i) {
        this.deleteCr(1, i);
    }

    protected void deleteCr(int z, int i) {
        if (i >= 0 && i < this.crSpec[z].length) {
            double[] cr = new double[this.crSpec[z].length - 1];
            System.arraycopy(this.crSpec[z], 0, cr, 0, i);
            System.arraycopy(this.crSpec[z], i + 1, cr, i, this.crSpec[z].length - i - 1);
            this.crSpec[z] = cr;
            ListIterator iterator = this.list.listIterator(0);

            while (iterator.hasNext()) {
                TableLayout.Entry entry = (TableLayout.Entry) iterator.next();
                if (entry.cr1[z] > i) {
                    --entry.cr1[z];
                }

                if (entry.cr2[z] > i) {
                    --entry.cr2[z];
                }
            }

            this.dirty = true;
        } else {
            throw new IllegalArgumentException("Parameter i is invalid.  i = " + i + ".  Valid range is [0, " + (this.crSpec[z].length - 1) + "].");
        }
    }

    public String toString() {
        String value = "TableLayout {{";
        int counter;
        if (this.crSpec[0].length > 0) {
            for (counter = 0; counter < this.crSpec[0].length - 1; ++counter) {
                value = value + this.crSpec[0][counter] + ", ";
            }

            value = value + this.crSpec[0][this.crSpec[0].length - 1] + "}, {";
        } else {
            value = value + "}, {";
        }

        if (this.crSpec[1].length > 0) {
            for (counter = 0; counter < this.crSpec[1].length - 1; ++counter) {
                value = value + this.crSpec[1][counter] + ", ";
            }

            value = value + this.crSpec[1][this.crSpec[1].length - 1] + "}}";
        } else {
            value = value + "}}";
        }

        return value;
    }

    public List getInvalidEntry() {
        LinkedList listInvalid = new LinkedList();

        try {
            ListIterator iterator = this.list.listIterator(0);

            while (true) {
                TableLayout.Entry entry;
                do {
                    if (!iterator.hasNext()) {
                        return listInvalid;
                    }

                    entry = (TableLayout.Entry) iterator.next();
                }
                while (entry.cr1[1] >= 0 && entry.cr1[0] >= 0 && entry.cr2[1] < this.crSpec[1].length && entry.cr2[0] < this.crSpec[0].length);

                listInvalid.add(entry.copy());
            }
        } catch (CloneNotSupportedException var4) {
            throw new RuntimeException("Unexpected CloneNotSupportedException");
        }
    }

    public List getOverlappingEntry() {
        LinkedList listOverlapping = new LinkedList();

        try {
            int numEntry = this.list.size();
            if (numEntry == 0) {
                return listOverlapping;
            } else {
                TableLayout.Entry[] entry = (TableLayout.Entry[]) ((TableLayout.Entry[]) this.list.toArray(new TableLayout.Entry[numEntry]));

                for (int knowUnique = 1; knowUnique < numEntry; ++knowUnique) {
                    for (int checking = knowUnique - 1; checking >= 0; --checking) {
                        if (entry[checking].cr1[0] >= entry[knowUnique].cr1[0] && entry[checking].cr1[0] <= entry[knowUnique].cr2[0] && entry[checking].cr1[1] >= entry[knowUnique].cr1[1] && entry[checking].cr1[1] <= entry[knowUnique].cr2[1] || entry[checking].cr2[0] >= entry[knowUnique].cr1[0] && entry[checking].cr2[0] <= entry[knowUnique].cr2[0] && entry[checking].cr2[1] >= entry[knowUnique].cr1[1] && entry[checking].cr2[1] <= entry[knowUnique].cr2[1]) {
                            listOverlapping.add(entry[checking].copy());
                        }
                    }
                }

                return listOverlapping;
            }
        } catch (CloneNotSupportedException var6) {
            throw new RuntimeException("Unexpected CloneNotSupportedException");
        }
    }

    protected void calculateSize(Container container) {
        Insets inset = container.getInsets();
        Dimension d = container.getSize();
        int availableWidth = d.width - inset.left - inset.right;
        int availableHeight = d.height - inset.top - inset.bottom;
        if (this.crSpec[0].length > 0) {
            availableWidth -= this.hGap * (this.crSpec[0].length - 1);
        }

        if (this.crSpec[1].length > 0) {
            availableHeight -= this.vGap * (this.crSpec[1].length - 1);
        }

        this.crSize[0] = new int[this.crSpec[0].length];
        this.crSize[1] = new int[this.crSpec[1].length];
        availableWidth = this.assignAbsoluteSize(0, availableWidth);
        availableHeight = this.assignAbsoluteSize(1, availableHeight);
        availableWidth = this.assignPrefMinSize(0, availableWidth, -3.0D);
        availableWidth = this.assignPrefMinSize(0, availableWidth, -2.0D);
        availableHeight = this.assignPrefMinSize(1, availableHeight, -3.0D);
        availableHeight = this.assignPrefMinSize(1, availableHeight, -2.0D);
        availableWidth = this.assignRelativeSize(0, availableWidth);
        availableHeight = this.assignRelativeSize(1, availableHeight);
        this.assignFillSize(0, availableWidth);
        this.assignFillSize(1, availableHeight);
        this.calculateOffset(0, inset);
        this.calculateOffset(1, inset);
        this.dirty = false;
        this.oldWidth = d.width;
        this.oldHeight = d.height;
    }

    protected int assignAbsoluteSize(int z, int availableSize) {
        int numCr = this.crSpec[z].length;

        for (int counter = 0; counter < numCr; ++counter) {
            if (this.crSpec[z][counter] >= 1.0D || this.crSpec[z][counter] == 0.0D) {
                this.crSize[z][counter] = (int) (this.crSpec[z][counter] + 0.5D);
                availableSize -= this.crSize[z][counter];
            }
        }

        return availableSize;
    }

    protected int assignRelativeSize(int z, int availableSize) {
        int relativeSize = availableSize < 0 ? 0 : availableSize;
        int numCr = this.crSpec[z].length;

        for (int counter = 0; counter < numCr; ++counter) {
            if (this.crSpec[z][counter] > 0.0D && this.crSpec[z][counter] < 1.0D) {
                this.crSize[z][counter] = (int) (this.crSpec[z][counter] * (double) relativeSize + 0.5D);
                availableSize -= this.crSize[z][counter];
            }
        }

        return availableSize;
    }

    protected void assignFillSize(int z, int availableSize) {
        if (availableSize > 0) {
            int numFillSize = 0;
            int numCr = this.crSpec[z].length;

            int slackSize;
            for (slackSize = 0; slackSize < numCr; ++slackSize) {
                if (this.crSpec[z][slackSize] == -1.0D) {
                    ++numFillSize;
                }
            }

            slackSize = availableSize;

            int counter;
            for (counter = 0; counter < numCr; ++counter) {
                if (this.crSpec[z][counter] == -1.0D) {
                    this.crSize[z][counter] = availableSize / numFillSize;
                    slackSize -= this.crSize[z][counter];
                }
            }

            for (counter = numCr - 1; counter >= 0 && slackSize > 0; --counter) {
                if (this.crSpec[z][counter] == -1.0D) {
                    ++this.crSize[z][counter];
                    --slackSize;
                }
            }

        }
    }

    protected void calculateOffset(int z, Insets inset) {
        int numCr = this.crSpec[z].length;
        this.crOffset[z] = new int[numCr + 1];
        this.crOffset[z][0] = z == 0 ? inset.left : inset.top;

        for (int counter = 0; counter < numCr; ++counter) {
            this.crOffset[z][counter + 1] = this.crOffset[z][counter] + this.crSize[z][counter];
        }

    }

    protected int assignPrefMinSize(int z, int availableSize, double typeOfSize) {
        int numCr = this.crSpec[z].length;

        label110:
        for (int counter = 0; counter < numCr; ++counter) {
            if (this.crSpec[z][counter] == typeOfSize) {
                int maxSize = 0;
                ListIterator iterator = this.list.listIterator(0);

                while (true) {
                    int size;
                    int numAdjustable;
                    label105:
                    while (true) {
                        TableLayout.Entry entry;
                        do {
                            do {
                                do {
                                    do {
                                        if (!iterator.hasNext()) {
                                            this.crSize[z][counter] = maxSize;
                                            availableSize -= maxSize;
                                            continue label110;
                                        }

                                        entry = (TableLayout.Entry) iterator.next();
                                    } while (entry.cr1[z] < 0);
                                } while (entry.cr2[z] >= numCr);
                            } while (entry.cr1[z] > counter);
                        } while (entry.cr2[z] < counter);

                        Dimension p = typeOfSize == -2.0D ? entry.component.getPreferredSize() : entry.component.getMinimumSize();
                        size = p == null ? 0 : (z == 0 ? p.width : p.height);
                        numAdjustable = 0;
                        int entryCr;
                        if (typeOfSize == -2.0D) {
                            entryCr = entry.cr1[z];

                            while (true) {
                                if (entryCr > entry.cr2[z]) {
                                    break label105;
                                }

                                if (this.crSpec[z][entryCr] < 0.0D && this.crSpec[z][entryCr] != -3.0D) {
                                    if (this.crSpec[z][entryCr] == -2.0D) {
                                        ++numAdjustable;
                                    } else if (this.crSpec[z][entryCr] == -1.0D) {
                                        break;
                                    }
                                } else {
                                    size -= this.crSize[z][entryCr];
                                }

                                ++entryCr;
                            }
                        } else {
                            entryCr = entry.cr1[z];

                            while (true) {
                                if (entryCr > entry.cr2[z]) {
                                    break label105;
                                }

                                if (this.crSpec[z][entryCr] >= 0.0D) {
                                    size -= this.crSize[z][entryCr];
                                } else if (this.crSpec[z][entryCr] != -2.0D && this.crSpec[z][entryCr] != -3.0D) {
                                    if (this.crSpec[z][entryCr] == -1.0D) {
                                        break;
                                    }
                                } else {
                                    ++numAdjustable;
                                }

                                ++entryCr;
                            }
                        }
                    }

                    size = (int) Math.ceil((double) size / (double) numAdjustable);
                    if (maxSize < size) {
                        maxSize = size;
                    }
                }
            }
        }

        return availableSize;
    }

    public void layoutContainer(Container container) {
        Dimension d = container.getSize();
        if (this.dirty || d.width != this.oldWidth || d.height != this.oldHeight) {
            this.calculateSize(container);
        }

        ComponentOrientation co = this.getComponentOrientation(container);
        boolean isRightToLeft = co != null && !co.isLeftToRight();
        Insets insets = container.getInsets();
        Component[] component = container.getComponents();

        for (int counter = 0; counter < component.length; ++counter) {
            try {
                ListIterator iterator = this.list.listIterator(0);

                TableLayout.Entry entry;
                for (entry = null; iterator.hasNext(); entry = null) {
                    entry = (TableLayout.Entry) iterator.next();
                    if (entry.component == component[counter]) {
                        break;
                    }
                }

                if (entry == null) {
                    component[counter].setBounds(0, 0, 0, 0);
                } else {
                    int preferredWidth = 0;
                    int preferredHeight = 0;
                    if (entry.alignment[0] != 2 || entry.alignment[1] != 2) {
                        Dimension preferredSize = component[counter].getPreferredSize();
                        preferredWidth = preferredSize.width;
                        preferredHeight = preferredSize.height;
                    }

                    int[] value = this.calculateSizeAndOffset(entry, preferredWidth, true);
                    int x = value[0];
                    int w = value[1];
                    value = this.calculateSizeAndOffset(entry, preferredHeight, false);
                    int y = value[0];
                    int h = value[1];
                    if (isRightToLeft) {
                        x = d.width - x - w + insets.left - insets.right;
                    }

                    component[counter].setBounds(x, y, w, h);
                }
            } catch (Exception var17) {
                component[counter].setBounds(0, 0, 0, 0);
            }
        }

    }

    protected ComponentOrientation getComponentOrientation(Container container) {
        ComponentOrientation co = null;

        try {
            if (checkForComponentOrientationSupport) {
                methodGetComponentOrientation = Class.forName("java.awt.Container").getMethod("getComponentOrientation");
                checkForComponentOrientationSupport = false;
            }

            if (methodGetComponentOrientation != null) {
                co = (ComponentOrientation) methodGetComponentOrientation.invoke(container);
            }
        } catch (Exception var4) {
            ;
        }

        return co;
    }

    protected int[] calculateSizeAndOffset(TableLayout.Entry entry, int preferredSize, boolean isColumn) {
        int[] crOffset = isColumn ? this.crOffset[0] : this.crOffset[1];
        int entryAlignment = isColumn ? entry.alignment[0] : entry.alignment[1];
        int cellSetSize = isColumn ? crOffset[entry.cr2[0] + 1] - crOffset[entry.cr1[0]] : crOffset[entry.cr2[1] + 1] - crOffset[entry.cr1[1]];
        int size;
        if (entryAlignment != 2 && cellSetSize >= preferredSize) {
            size = preferredSize;
        } else {
            size = cellSetSize;
        }

        if (isColumn && entryAlignment == 4) {
            entryAlignment = 0;
        }

        if (isColumn && entryAlignment == 5) {
            entryAlignment = 3;
        }

        int offset;
        switch (entryAlignment) {
            case 0:
                offset = crOffset[isColumn ? entry.cr1[0] : entry.cr1[1]];
                break;
            case 1:
                offset = crOffset[isColumn ? entry.cr1[0] : entry.cr1[1]] + (cellSetSize - size >> 1);
                break;
            case 2:
                offset = crOffset[isColumn ? entry.cr1[0] : entry.cr1[1]];
                break;
            case 3:
                offset = crOffset[(isColumn ? entry.cr2[0] : entry.cr2[1]) + 1] - size;
                break;
            default:
                offset = 0;
        }

        int cumlativeGap;
        if (isColumn) {
            offset += this.hGap * entry.cr1[0];
            cumlativeGap = this.hGap * (entry.cr2[0] - entry.cr1[0]);
            switch (entryAlignment) {
                case 1:
                    offset += cumlativeGap >> 1;
                    break;
                case 2:
                    size += cumlativeGap;
                    break;
                case 3:
                    offset += cumlativeGap;
            }
        } else {
            offset += this.vGap * entry.cr1[1];
            cumlativeGap = this.vGap * (entry.cr2[1] - entry.cr1[1]);
            switch (entryAlignment) {
                case 1:
                    offset += cumlativeGap >> 1;
                    break;
                case 2:
                    size += cumlativeGap;
                    break;
                case 3:
                    offset += cumlativeGap;
            }
        }

        int[] value = new int[]{offset, size};
        return value;
    }

    public Dimension preferredLayoutSize(Container container) {
        return this.calculateLayoutSize(container, -2.0D);
    }

    public Dimension minimumLayoutSize(Container container) {
        return this.calculateLayoutSize(container, -3.0D);
    }

    protected Dimension calculateLayoutSize(Container container, double typeOfSize) {
        TableLayout.Entry[] entryList = (TableLayout.Entry[]) ((TableLayout.Entry[]) this.list.toArray(new TableLayout.Entry[this.list.size()]));
        int numEntry = entryList.length;
        Dimension[] prefMinSize = new Dimension[numEntry];

        int width;
        for (width = 0; width < numEntry; ++width) {
            prefMinSize[width] = typeOfSize == -2.0D ? entryList[width].component.getPreferredSize() : entryList[width].component.getMinimumSize();
        }

        width = this.calculateLayoutSize(container, 0, typeOfSize, entryList, prefMinSize);
        int height = this.calculateLayoutSize(container, 1, typeOfSize, entryList, prefMinSize);
        Insets inset = container.getInsets();
        width += inset.left + inset.right;
        height += inset.top + inset.bottom;
        return new Dimension(width, height);
    }

    protected int calculateLayoutSize(Container container, int z, double typeOfSize, TableLayout.Entry[] entryList, Dimension[] prefMinSize) {
        int scaledSize = 0;
        int numCr = this.crSpec[z].length;
        double fillSizeRatio = 1.0D;
        int numFillSize = 0;

        int counter;
        for (counter = 0; counter < numCr; ++counter) {
            if (this.crSpec[z][counter] > 0.0D && this.crSpec[z][counter] < 1.0D) {
                fillSizeRatio -= this.crSpec[z][counter];
            } else if (this.crSpec[z][counter] == -1.0D) {
                ++numFillSize;
            }
        }

        if (numFillSize > 1) {
            fillSizeRatio /= (double) numFillSize;
        }

        if (fillSizeRatio < 0.0D) {
            fillSizeRatio = 0.0D;
        }

        this.crSize[z] = new int[numCr];
        this.assignAbsoluteSize(z, 0);
        this.assignPrefMinSize(z, 0, -3.0D);
        this.assignPrefMinSize(z, 0, -2.0D);
        int[] crPrefMin = new int[numCr];

        for (counter = 0; counter < numCr; ++counter) {
            if (this.crSpec[z][counter] == -2.0D || this.crSpec[z][counter] == -3.0D) {
                crPrefMin[counter] = this.crSize[z][counter];
            }
        }

        int numColumn = this.crSpec[0].length;
        int numRow = this.crSpec[1].length;
        int numEntry = entryList.length;

        int totalSize;
        for (totalSize = 0; totalSize < numEntry; ++totalSize) {
            TableLayout.Entry entry = entryList[totalSize];
            if (entry.cr1[0] >= 0 && entry.cr1[0] < numColumn && entry.cr2[0] < numColumn && entry.cr1[1] >= 0 && entry.cr1[1] < numRow && entry.cr2[1] < numRow) {
                Dimension size = prefMinSize[totalSize];
                int scalableSize = z == 0 ? size.width : size.height;

                for (counter = entry.cr1[z]; counter <= entry.cr2[z]; ++counter) {
                    if (this.crSpec[z][counter] >= 1.0D) {
                        scalableSize = (int) ((double) scalableSize - this.crSpec[z][counter]);
                    } else if (this.crSpec[z][counter] == -2.0D || this.crSpec[z][counter] == -3.0D) {
                        scalableSize -= crPrefMin[counter];
                    }
                }

                double relativeSize = 0.0D;

                for (counter = entry.cr1[z]; counter <= entry.cr2[z]; ++counter) {
                    if (this.crSpec[z][counter] > 0.0D && this.crSpec[z][counter] < 1.0D) {
                        relativeSize += this.crSpec[z][counter];
                    } else if (this.crSpec[z][counter] == -1.0D && fillSizeRatio != 0.0D) {
                        relativeSize += fillSizeRatio;
                    }
                }

                int temp;
                if (relativeSize == 0.0D) {
                    temp = 0;
                } else {
                    temp = (int) ((double) scalableSize / relativeSize + 0.5D);
                }

                if (scaledSize < temp) {
                    scaledSize = temp;
                }
            }
        }

        totalSize = scaledSize;

        for (counter = 0; counter < numCr; ++counter) {
            if (this.crSpec[z][counter] >= 1.0D) {
                totalSize += (int) (this.crSpec[z][counter] + 0.5D);
            } else if (this.crSpec[z][counter] == -2.0D || this.crSpec[z][counter] == -3.0D) {
                totalSize += crPrefMin[counter];
            }
        }

        if (numCr > 0) {
            totalSize += (z == 0 ? this.hGap : this.vGap) * (numCr - 1);
        }

        return totalSize;
    }

    public void addLayoutComponent(String name, Component component) {
        this.addLayoutComponent((Component) component, (Object) name);
    }

    public void addLayoutComponent(Component component, Object constraint) {
        if (constraint instanceof String) {
            Object constraints = new TableLayoutConstraints((String) constraint);
            this.list.add(new TableLayout.Entry(component, (TableLayoutConstraints) constraints));
            this.dirty = true;
        } else {
            if (!(constraint instanceof TableLayoutConstraints)) {
                if (constraint == null) {
                    throw new IllegalArgumentException("No constraint for the component");
                }

                throw new IllegalArgumentException("Cannot accept a constraint of class " + constraint.getClass());
            }

            this.list.add(new TableLayout.Entry(component, (TableLayoutConstraints) constraint));
            this.dirty = true;
        }

    }

    public void removeLayoutComponent(Component component) {
        ListIterator iterator = this.list.listIterator(0);

        while (iterator.hasNext()) {
            TableLayout.Entry entry = (TableLayout.Entry) iterator.next();
            if (entry.component == component) {
                iterator.remove();
            }
        }

        this.dirty = true;
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public float getLayoutAlignmentX(Container parent) {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container parent) {
        return 0.5F;
    }

    public void invalidateLayout(Container target) {
        this.dirty = true;
    }

    public static class Entry implements Cloneable {
        public Component component;
        public int[] cr1;
        public int[] cr2;
        public int[] alignment;

        public Entry(Component component, TableLayoutConstraints constraint) {
            int[] cr1 = new int[]{constraint.col1, constraint.row1};
            int[] cr2 = new int[]{constraint.col2, constraint.row2};
            int[] alignment = new int[]{constraint.hAlign, constraint.vAlign};
            this.cr1 = cr1;
            this.cr2 = cr2;
            this.alignment = alignment;
            this.component = component;
        }

        public Object copy() throws CloneNotSupportedException {
            return this.clone();
        }

        public String toString() {
            TableLayoutConstraints c = new TableLayoutConstraints(this.cr1[0], this.cr1[1], this.cr2[0], this.cr2[1], this.alignment[0], this.alignment[1]);
            return "(" + c + ") " + this.component;
        }
    }
}
