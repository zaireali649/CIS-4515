package com.templecis.escaperoute.Maze_Stuff;

public class Intcoll {

    //constructor should be the same, documentation same for intcoll4
    private BtNode c;
    private int how_many;

    public Intcoll(int j) {
        //Has no parameters, make sure collection is empty, and has space for 500 ints
        c = null;
        how_many = 0;
    }

    private static BtNode Copytree(BtNode t) {
        BtNode L = null;
        BtNode R = null;
        BtNode root = null;

        if (t != null) {
            L = Copytree(t.left);
            R = Copytree(t.right);
            root = new BtNode(t.info, L, R);
        }
        return root;
    }

    public void copy(Intcoll obj) {
        //copy integers from object and put into obj; p.copy(Q). p = obj
        if (this != obj) {
            how_many = obj.get_howmany();
            c = Copytree(obj.c);
        }
    }

    public boolean belongs(int i) {
        //Enters an int, tests where the number is in a set of numbers given, returns false if its not there
        BtNode p = c;
        while ((p != null) && (p.info != i)) {
            if (i < p.info) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        return (p != null);
    }

    public void insert(int i) {
        //Inserts I if not in collection, if in collection does nothing

        BtNode p = c;
        BtNode pred = null;
        while ((p != null) && (p.info != i)) {
            pred = p;
            if (i < p.info) {
                p = p.left;

            } else {
                p = p.right;
            }
        }
        if (p == null) {
            how_many++;
            p = new BtNode(i, null, null);
            if (pred != null) {
                if (i < pred.info) {
                    pred.left = p;
                } else {
                    pred.right = p;
                }

            } else {
                c = p;
            }
        }
    }

    public void omit(int i) {
        //Remove I from collection if it's there, if it's not there, do nothing
        BtNode p = c;
        BtNode pred = null;
        while ((p != null) && (p.info != i)) {
            pred = p;
            if (i < p.info) {
                p = p.left;
            }
            if (i > p.info) {
                p = p.right;
            }
        }
        if (p != null) {
            how_many--;
            if (pred != null) {
                if ((p.left == null) && (p.right == null)) {
                    if (p.info > pred.info) {
                        pred.right = null;
                    } else {
                        pred.left = null;
                    }

                } else if ((p.right != null) && (p.left == null)) {
                    if (p.info < pred.info) {
                        pred.left = p.right;
                    } else {
                        pred.right = p.right;
                    }
                } else if ((p.left != null) && (p.right == null)) {
                    if (p.info > pred.info) {
                        pred.right = p.left;
                    } else {
                        pred.left = p.left;
                    }
                } else if ((p.left != null) && (p.right != null)) {
                    if (p.info > pred.info) {
                        BtNode m = p;
                        BtNode n = pred;
                        while (m.left != null) {
                            n = m;
                            m = m.left;
                        }
                        p.info = m.info;
                        n.left = null;

                    }
                    if (p.info < pred.info) {
                        BtNode m = p;
                        BtNode n = p;
                        while (m.right != null) {
                            n = m;
                            m = m.right;
                        }
                        p.info = m.info;
                        n.right = null;
                    }
                }
            }
            if (pred == null) {
                if ((p.left == null) && (p.right == null)) {
                    c = null;
                } else if ((p.left != null) && (p.right == null)) {
                    c = p.left;
                } else if ((p.left == null) && (p.right != null)) {
                    c = p.right;
                } else if ((p.right != null) && (p.left != null)) {
                    BtNode m = p;
                    BtNode n = pred;
                    while (m.right != null) {
                        n = m;
                        m = m.right;
                    }
                    p.info = m.info;
                    n.right = null;
                }
            }
        }
    }

    public int get_howmany() {
        //Returns how many numbers are in the collection
        return how_many;
    }

    private static void printtree(BtNode t) {
        if (t != null) {
            printtree(t.left);
            System.out.println(t.info);
            printtree(t.right);

        }
    }

    public void print() {
        printtree(c);
    }

    private static int toArray(BtNode t, int[] a, int i) {
        int num = 0;
        if (t != null) {
            num = toArray(t.left, a, i);
            a[num + 1] = t.info;
            num = num + 1 + toArray(t.right, a, num + 1 + i);
        }
        return num;
    }

    public boolean equals(Intcoll obj) {
        //Returns true if integers in positive are the same as the integers in the other collection
        boolean result = (how_many == obj.how_many);
        int[] A = new int[how_many];
        int[] B = new int[how_many];
        if (result) {

            toArray(c, A, 0);
            toArray(obj.c, B, 0);
        }
        int k = 0;
        while ((result) && (k < how_many)) {
            result = (A[k] == B[k]);
            k++;
        }
        return result;
    }

    private static class BtNode {

        private int info;
        private BtNode left;
        private BtNode right;

        private BtNode() {
            info = 0;
            left = null;
            right = null;
        }

        private BtNode(int s, BtNode lt, BtNode rt) {
            info = s;
            left = lt;
            right = rt;
        }

    }
}