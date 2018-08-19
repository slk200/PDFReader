package org.tizzer.pdfreader.layout;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class TableLayoutConstraints implements TableLayoutConstants {
    public int col1;
    public int row1;
    public int col2;
    public int row2;
    public int hAlign;
    public int vAlign;

    public TableLayoutConstraints() {
        this.col1 = this.row1 = this.col2 = 0;
        this.hAlign = this.vAlign = 2;
    }

    public TableLayoutConstraints(int col, int row) {
        this(col, row, col, row, 2, 2);
    }

    public TableLayoutConstraints(int col1, int row1, int col2, int row2) {
        this(col1, row1, col2, row2, 2, 2);
    }

    public TableLayoutConstraints(int col1, int row1, int col2, int row2, int hAlign, int vAlign) {
        this.col1 = col1;
        this.row1 = row1;
        this.col2 = col2;
        this.row2 = row2;
        if (hAlign != 0 && hAlign != 3 && hAlign != 1 && hAlign != 2 && hAlign != 5) {
            this.hAlign = 2;
        } else {
            this.hAlign = hAlign;
        }

        if (vAlign != 0 && vAlign != 3 && vAlign != 1) {
            this.vAlign = 2;
        } else {
            this.vAlign = vAlign;
        }

    }

    public TableLayoutConstraints(String constraints) {
        this.col1 = 0;
        this.row1 = 0;
        this.col2 = 0;
        this.row2 = 0;
        this.hAlign = 2;
        this.vAlign = 2;
        StringTokenizer st = new StringTokenizer(constraints, ", ");
        int numToken = st.countTokens();

        try {
            if (numToken != 2 && numToken != 4 && numToken != 6) {
                throw new RuntimeException();
            }

            String tokenA = st.nextToken();
            this.col1 = new Integer(tokenA);
            this.col2 = this.col1;
            String tokenB = st.nextToken();
            this.row1 = new Integer(tokenB);
            this.row2 = this.row1;
            tokenA = st.nextToken();
            tokenB = st.nextToken();

            try {
                this.col2 = new Integer(tokenA);
                this.row2 = new Integer(tokenB);
                tokenA = st.nextToken();
                tokenB = st.nextToken();
            } catch (NumberFormatException var7) {
                this.col2 = this.col1;
                this.row2 = this.row1;
            }

            if (!tokenA.equalsIgnoreCase("L") && !tokenA.equalsIgnoreCase("LEFT")) {
                if (!tokenA.equalsIgnoreCase("C") && !tokenA.equalsIgnoreCase("CENTER")) {
                    if (!tokenA.equalsIgnoreCase("F") && !tokenA.equalsIgnoreCase("FULL")) {
                        if (!tokenA.equalsIgnoreCase("R") && !tokenA.equalsIgnoreCase("RIGHT")) {
                            if (!tokenA.equalsIgnoreCase("LD") && !tokenA.equalsIgnoreCase("LEADING")) {
                                if (!tokenA.equalsIgnoreCase("TL") && !tokenA.equalsIgnoreCase("TRAILING")) {
                                    throw new RuntimeException();
                                }

                                this.hAlign = 5;
                            } else {
                                this.hAlign = 4;
                            }
                        } else {
                            this.hAlign = 3;
                        }
                    } else {
                        this.hAlign = 2;
                    }
                } else {
                    this.hAlign = 1;
                }
            } else {
                this.hAlign = 0;
            }

            if (!tokenB.equalsIgnoreCase("T") && !tokenB.equalsIgnoreCase("TOP")) {
                if (!tokenB.equalsIgnoreCase("C") && !tokenB.equalsIgnoreCase("CENTER")) {
                    if (!tokenB.equalsIgnoreCase("F") && !tokenB.equalsIgnoreCase("FULL")) {
                        if (!tokenB.equalsIgnoreCase("B") && !tokenB.equalsIgnoreCase("BOTTOM")) {
                            throw new RuntimeException();
                        }

                        this.vAlign = 3;
                    } else {
                        this.vAlign = 2;
                    }
                } else {
                    this.vAlign = 1;
                }
            } else {
                this.vAlign = 0;
            }
        } catch (NoSuchElementException var8) {
            ;
        } catch (RuntimeException var9) {
            throw new IllegalArgumentException("Expected constraints in one of the following formats:\n  col1, row1\n  col1, row1, col2, row2\n  col1, row1, hAlign, vAlign\n  col1, row1, col2, row2, hAlign, vAlign\nConstraints provided '" + constraints + "'");
        }

        if (this.row2 < this.row1) {
            this.row2 = this.row1;
        }

        if (this.col2 < this.col1) {
            this.col2 = this.col1;
        }

    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.col1);
        buffer.append(", ");
        buffer.append(this.row1);
        buffer.append(", ");
        buffer.append(this.col2);
        buffer.append(", ");
        buffer.append(this.row2);
        buffer.append(", ");
        String[] h = new String[]{"left", "center", "full", "right", "leading", "trailing"};
        String[] v = new String[]{"top", "center", "full", "bottom"};
        buffer.append(h[this.hAlign]);
        buffer.append(", ");
        buffer.append(v[this.vAlign]);
        return buffer.toString();
    }
}
