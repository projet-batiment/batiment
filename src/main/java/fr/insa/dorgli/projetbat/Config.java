import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

enum ParseHead {
    POINTS,
    TYPESREVETEMENT,
    MURS,
    REVETEMENTSMUR,
    PIECES,

    SKETCHVERSION,
    USEDVERSION,

    // TODO
    REVETEMENT,
    APPARTS,
    ETAGEAPPARTS,
    NIVEAUX,
    NONE,
}

public class Config {
    // hashmaps ou arraylists ? TODO
    // -> hashmap: plus facile à maitriser
    // -> arraylists: les instances ont accès à leurs ids respectifs
    private HashMap<Integer, Point> points;
    private HashMap<Integer, TypeRevetement> typesRevetement;
    private HashMap<Integer, Mur> murs;
    private HashMap<Integer, Piece> pieces;

    public final int version = 1;               // software global/current version
    public final String progname = "Batiment";  // software's name
    public int sketchVersion;                   // UX

    private int idCounter; // un int suffit (allez trouver quelqu'un qui créera 2 147 483 647 objets dans ce programme)
    private int nextId() {
        idCounter++;
        return idCounter - 1; // -1 because there's no defer in java
    }

    // basic config stuff
    public Config() {
        points = new HashMap<Integer, Point>();
        typesRevetement = new HashMap<Integer, TypeRevetement>();
        murs = new HashMap<Integer, Mur>();
        pieces = new HashMap<Integer, Piece>();

        sketchVersion = 0;
    }
    public String toString() { // TODO: depths
        // begining of the string + points
        String out = new String("Config {\n" + "  points: {\n");
        for (int key: points.keySet()) {
            out += "    #" + key + ": " + points.get(key) + ",\n";
        }

        // typesRevetement
        out += "  },\n  typesRevetement: {\n";
        for (int key: typesRevetement.keySet()) {
            out += "    #" + key + ": " + typesRevetement.get(key) + ",\n";
        }

        // murs
        out += "  },\n  murs: {\n";
        for (int key: murs.keySet()) {
            out += "    #" + key + ": " + murs.get(key).toString(2) + ",\n";
        }

        // pieces
        out += "  },\n  pieces: {\n";
        for (int key: pieces.keySet()) {
            out += "    #" + key + ": " + pieces.get(key) + ",\n";
        }

        // end
        return out + "  },\n}";
    }

    /// some random utils here
    // Ecode/decode escape sequences into strings (for the config files)
    public String decodeEscaped(String escaped) {
        if (escaped.equals("&0")) return "";    // empty strings need to be escaped in the config file...

        return escaped
            .replaceAll("&&", "&")              // we use '&' as escape character
            .replaceAll("&v", ",")              // virgule -> &v
            .replaceAll("&n", "\n")             // newline -> &n
            .replaceAll("&r", "\r")             // CR -> &n
        ;
    }
    public String encodeEscaped(String text) {
        if (text.isEmpty()) return "&0";        // empty strings need to be escaped in the config file...

        return text
            .replaceAll("&", "&&")  // <-- !! should remain first
            .replaceAll(",", "&v")
            .replaceAll("\n", "&n")
            .replaceAll("\r", "&r")
        ;
    }

    // parse a string into the current config instance
    public int parseConfig(String text) {
        System.out.println("INF: parseConfig: starting config parsing");
        String[] lines = text.split("\n");

        ParseHead head = ParseHead.NONE;
        int errcount = 0;

        int tmpA = 0;   // illegal temporary variables for multilined entries to store temporary values
        int tmpB = 0;

        for (int i=0; i<lines.length; i++) {
            String line = lines[i];

            if (line.startsWith("!!")) {
                String command = line.substring(2);
                try {
                    head = ParseHead.valueOf(command.toUpperCase());
                    System.out.println("INF: parseConfig: head set to " + head);
                } catch (Exception e) {
                    // switch (command) {
                    //     default:
                            System.out.println("ERR: parseConfig: unknown config command '" + line + "'");
                            errcount++;
                    //         break;
                    // }
                }

            // } else if (line.equals("!!Points")) {
            //     System.out.println("INF: parseConfig: reading points");
            //     head = ParseHead.POINTS;
            //
            // } else if (line.equals("!!Murs")) {
            //     System.out.println("INF: parseConfig: reading murs");
            //     head = ParseHead.MURS;

            } else {
                switch (head) {
                    // --- objets du batiment

                    case POINTS:
                        // syntax: x,y,niveau,id
                        try {
                            String[] splitted = line.split(",");

                            int x = Integer.parseInt(splitted[0]);
                            int y = Integer.parseInt(splitted[1]);
                            int niveau = Integer.parseInt(splitted[2]);
                            int id = Integer.parseInt(splitted[3]);

                            newPoint(x, y, niveau, id);
                            System.out.println("DBG: parseConfig: loaded point '" + line + "'");
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: failed loading point '" + line + "': " + e.getMessage());
                            errcount++;
                        }
                        break;

                    case TYPESREVETEMENT:
                        // syntax: prixUnitaire,nom,description,id
                        try {
                            String[] splitted = line.split(",");

                            float prixUnitaire = Float.parseFloat(splitted[0]);
                            int id = Integer.parseInt(splitted[2]);

                            newTypeRevetement(prixUnitaire, decodeEscaped(splitted[1]), decodeEscaped(splitted[2]), id);
                            System.out.println("DBG: parseConfig: loaded typerevetement '" + line + "'");
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: failed loading typerevetement '" + line + "': " + e.getMessage());
                            errcount++;
                        }
                        break;

                    case MURS:
                        // syntax: pointDebutId,pointFinId,hauteur,id,
                        try {
                            String[] splitted = line.split(",");

                            int pointDebutId = Integer.parseInt(splitted[0]);
                            int pointFinId = Integer.parseInt(splitted[1]);
                            int hauteur = Integer.parseInt(splitted[2]);
                            int id = Integer.parseInt(splitted[3]);

                            newMur(pointDebutId, pointFinId, hauteur, id);
                            System.out.println("DBG: parseConfig: loaded and expecting revetements for mur '" + line + "'");

                            head = ParseHead.REVETEMENTSMUR;
                            tmpA = id;  // murID
                            tmpB = 1;   // which revetement (1 or 2)
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: failed loading mur '" + line + "': " + e.getMessage());
                            errcount++;
                        }
                        break;
                    case REVETEMENTSMUR:
                        // syntax: typeRevetementId,surface (info: surface=TODO)
                        // info: line '.' means next
                        if (line.equals(".")) {
                            if (tmpB == 1) {
                                tmpB = 2; // -> cote 2
                                System.out.println("DBG: parseConfig: reading murId " + tmpA + " cote 2");
                            }
                            else {
                                head = ParseHead.MURS; // -> back to mur
                                System.out.println("DBG: parseConfig: closed murId " + tmpA);
                            }
                            break;
                        }

                        try {
                            String[] splitted = line.split(",");

                            int type = Integer.parseInt(splitted[0]);
                            float surface = Float.parseFloat(splitted[1]);

                            addRevetementMur(getMur(tmpA), tmpB == 2, getTypeRevetement(type), surface);
                            System.out.println("DBG: parseConfig: loaded revetement '" + line + "' into murId " + tmpA);
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: failed loading revetementMur '" + line + "': " + e.getMessage());
                            errcount++;
                        }
                        break;

                    case PIECES:
                        // syntax: niveau,id,murIds
                        // note: murIds are colon-separated (:)
                        try {
                            String[] splitted = line.split(",");

                            int niveau = Integer.parseInt(splitted[0]);
                            int id = Integer.parseInt(splitted[1]);
                            String murIdsString = splitted[2].split(":");
                            int murIds = new int[murIdsString.length];

                            for (int i = 0; i < murIdsString.length; i++) {
                                murIds[i] = Integer.parseInt(murIdsString[i]);
                                // TODO
                            }

                            // newMur(pointDebutId, pointFinId, hauteur, id);
                            // System.out.println("DBG: parseConfig: loaded and expecting revetements for mur '" + line + "'");
                            //
                            // head = ParseHead.REVETEMENTSMUR;
                            // tmpA = id;  // murID
                            // tmpB = 1;   // which revetement (1 or 2)
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: failed loading mur '" + line + "': " + e.getMessage());
                            errcount++;
                        }
                        break;
                    // case REVETEMENTSSOLPLAFOND:
                    //     // syntax: typeRevetementId,surface (info: surface=TODO)
                    //     // info: line '.' means next
                    //     if (line.equals(".")) {
                    //         if (tmpB == 1) {
                    //             tmpB = 2; // -> cote 2
                    //             System.out.println("DBG: parseConfig: reading murId " + tmpA + " cote 2");
                    //         }
                    //         else {
                    //             head = ParseHead.MURS; // -> back to mur
                    //             System.out.println("DBG: parseConfig: closed murId " + tmpA);
                    //         }
                    //         break;
                    //     }
                    //
                    //     try {
                    //         String[] splitted = line.split(",");
                    //
                    //         int type = Integer.parseInt(splitted[0]);
                    //         float surface = Float.parseFloat(splitted[1]);
                    //
                    //         addRevetementMur(getMur(tmpA), tmpB == 2, getTypeRevetement(type), surface);
                    //         System.out.println("DBG: parseConfig: loaded revetement '" + line + "' into murId " + tmpA);
                    //     } catch (Exception e) {
                    //         System.out.println("ERR: parseConfig: failed loading revetementMur '" + line + "': " + e.getMessage());
                    //         errcount++;
                    //     }
                    //     break;


                    // --- autres

                    // required minimal software version for this file
                    case USEDVERSION:
                        try {
                            int usedVersion = Integer.parseInt(line);
                            if (usedVersion > version) {
                                System.out.println("ERR: parseConfig: this file was created with a newer version of " + progname + ": " + usedVersion + " > " + version);
                                errcount++;
                            } else
                                System.out.println("DBG: parseConfig: this file's version fits the current software: " + usedVersion + " <= " + version);
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: wrong cmd_usedversion-line syntax: '" + line + "'");
                            errcount++;
                        }
                        break;

                    // UX version of this sketch
                    case SKETCHVERSION:
                        try {
                            sketchVersion = Integer.parseInt(line);
                            System.out.println("DBG: parseConfig: set sketchversion to " + sketchVersion);
                        } catch (Exception e) {
                            System.out.println("ERR: parseConfig: wrong cmd_sketchversion-line syntax: '" + line + "'");
                            errcount++;
                        }
                        break;

                    default:
                        System.out.println("ERR: parseConfig: unknown behaviour " + head + ": '" + line + "'");
                        errcount++;
                        break;
                }
            }
        }

        System.out.println("INF: parseConfig: finished parsing with " + errcount + " error(s)");
        System.out.println("DBG: parseConfig: logging configuration:");
        System.out.println(this);
        return errcount;
    }

    /// subclass Point (private) and its stuff
    // utils:
    //   public int newPoint(int x, int y, int niveau) throws Exception
    //   private int newPoint(int x, int y, int niveau, id) throws Exception
    //   public Point getPoint(int id)
    //   public Integer getPointId(Point p)
    //   public Integer getPointId(int x, int y, int niveau)
    //   public Set<Integer> listPoints()
    private class Point {
        int x;
        int y;
        int niveau;

        public Point(int ex, int ey, int eniveau) {
            x = ex;
            y = ey;
            niveau = eniveau;
        }
        public String toString() {
            return "Point { x: " + x + ", y: " + y + ", niveau: " + niveau + " }";
        }
        public boolean equals(int ex, int ey, int eniveau) {
            return ( x == ex && y == ey && niveau == eniveau);
        }
        public boolean equals(Point p) {
            return ( x == p.x && y == p.y && niveau == p.niveau);
        }
    }
    // newPoint with newId, useful when parsing config (maybe public ?)
    private int newPoint(int x, int y, int niveau, int newId) throws Exception {
        // check if this point already exists
        Integer already = getPointId(x, y, niveau);
        if (already != null)
            throw new Exception("a Point with the same proprieties already exists: " + already + " -> " + points.get(already));

        // check if this ID is available
        if (points.containsKey(newId))
            throw new Exception("a Point with id " + newId + " already exists");

        // actually create the Point
        Point p = new Point(x, y, niveau);

        points.put(newId, p);
        return newId;
    }
    // create a new Point
    public int newPoint(int x, int y, int niveau) throws Exception {
        return newPoint(x, y, niveau, nextId());
    }
    // get a Point by ID
    public Point getPoint(int id) {
        if (points.containsKey(id))
            return points.get(id);
        else
            return null;
    }
    // search for a Point ID according to the Mur object
    public Integer getPointId(Point p) {
        return getPointId(p.x, p.y, p.niveau);
    }
    // search for a Point ID according to given pointDebut, pointFin and hauteur
    public Integer getPointId(int x, int y, int niveau) {
        for (int eachId: points.keySet()) {
            Point p = points.get(eachId);
            if (p.x == x && p.y == y && p.niveau == niveau)
                return eachId;
        }
        return null;
    }
    // return a set of Point IDs
    public Set<Integer> listPoints() {
        return points.keySet();
    }

    /// subclass Mur (private) and its stuff
    // utils:
    //   public int newMur(int pointDebut, int pointFin, int hauteur, int id) throws Exception
    //   public int newMur(int pointDebut, int pointFin, int hauteur) throws Exception
    //   public Mur getMur(int id)
    //   public Integer getMurId(Mur m)
    //   public Integer getMurId(Point pointDebut, Point pointFin, int hauteur)
    //   public Set<Integer> listMurs()
    private class Mur {
        Point pointDebut;
        Point pointFin;
        int hauteur;
        ArrayList<RevetementMur> revetements1;
        ArrayList<RevetementMur> revetements2;

        public Mur(Point epointDebut, Point epointFin, int ehauteur) {
            pointDebut = epointDebut;
            pointFin = epointFin;
            hauteur = ehauteur;
            revetements1 = new ArrayList<RevetementMur>();
            revetements2 = new ArrayList<RevetementMur>();
        }
        public String toString() {
            return toString(0);
        }
        public String toString(int depth) {
            String pfx = "";
            for (int i = 0; i <= depth; i++) {
                pfx += "  ";
            }
            int nextDepth = depth + 1;

            String revetements1Out = "[ ";
            for (RevetementMur each: revetements1) {
                revetements1Out += each.toStringShort() + ", ";
            }
            revetements1Out += "]";

            String revetements2Out = "[ ";
            for (RevetementMur each: revetements2) {
                revetements2Out += each.toStringShort() + ", ";
            }
            revetements2Out += "]";

            return "Mur {\n"
                + pfx + "pointDebut: " + pointDebut + ",\n"
                + pfx + "pointFin: " + pointFin + ",\n"
                + pfx + "hauteur: " + hauteur + ",\n"
                + pfx + "revetements1: " + revetements1Out + ",\n"
                + pfx + "revetements2: " + revetements2Out + ",\n"
                + pfx + "}";
        }
        public String toStringShort() {
            return "( #" + getMurId(pointDebut, pointFin) + ")";
        }
        public void addRevetement(RevetementMur r, boolean coteDeux) {
            if (coteDeux)
                revetements2.add(r);
            else
                revetements1.add(r);
        }

        // the functions 'compate' aren't called 'equals' because they voluntarly discare about 'hauteur'
        public boolean compare(int epointDebutId, int epointFinId) {
            Point epointDebut = points.get(epointDebutId);
            Point epointFin = points.get(epointFinId);
            if (epointDebut == null || epointFin == null) return false;
            return (pointDebut.equals(epointDebut) && pointFin.equals(epointFin));
        }
        public boolean compare(Mur m) {
            return (pointDebut.equals(m.pointDebut) && pointFin.equals(m.pointFin));
        }
    }
    // create a new Mur with newId, useful when parsing config (maybe public ?)
    private int newMur(Point pointDebut, Point pointFin, int hauteur, int newId) throws Exception {
        // check if this ID is available
        if (murs.containsKey(newId))
            throw new Exception("a Mur with id " + newId + " already exists");

        // check if the points are on the same level
        if (pointDebut.niveau != pointFin.niveau)
            throw new Exception("the two indicated points are on different levels (" + pointDebut.niveau + " and " + pointFin.niveau + ")");

        // check if this mur already exists
        Integer already = getMurId(pointDebut, pointFin);
        if (already != null)
            throw new Exception("a Mur with the same proprieties already exists: " + already + " -> " + murs.get(already));

        // actually create the mur
        Mur m = new Mur(pointDebut, pointFin, hauteur);

        murs.put(newId, m);
        return newId;
    }
    // create a new Mur with newId and id-addressed Points, useful when parsing config
    // (should stay private because of dealing with ids)
    private int newMur(int pointDebutId, int pointFinId, int hauteur, int newId) throws Exception {
        Point pointDebut = getPoint(pointDebutId);
        Point pointFin = getPoint(pointFinId);

        // check if the Points exist
        if (pointDebut == null)
            throw new Exception("Point " + pointDebutId + " (pointDebut) does not exist");
        if (pointFin == null)
            throw new Exception("Point " + pointFinId + " (pointFin) does not exist");

        return newMur(pointDebut, pointFin, hauteur, newId);
    }
    // create a new Mur
    public int newMur(Point pointDebut, Point pointFin, int hauteur) throws Exception {
        return newMur(pointDebut, pointFin, hauteur, nextId());
    }
    // get a Mur by ID
    public Mur getMur(int id) {
        if (murs.containsKey(id))
            return murs.get(id);
        else
            return null;
    }
    // search for a Mur ID according to the Mur object
    public Integer getMurId(Mur m) {
        return getMurId(m.pointDebut, m.pointFin);
    }
    // search for a Mur ID according to given pointDebut, pointFin and hauteur
    public Integer getMurId(Point pointDebut, Point pointFin) {
        for (int eachId: murs.keySet()) {
            Mur m = murs.get(eachId);
            if (pointDebut.equals(m.pointDebut) && pointFin.equals(m.pointFin))
                return eachId;
        }
        return null;
    }
    // return a set of Mur IDs
    public Set<Integer> listMurs() {
        return murs.keySet();
    }

    /// subclass TypeRevetement (private) and its stuff
    // describes a type of revetement
    // utils:
    //   public TypeRevetement newTypeRevetement(float eprix, String enom, String edescription, int newId) throws Exception
    //   public TypeRevetement newTypeRevetement(float eprix, String enom, String edescription) throws Exception
    //   public Integer getTypeRevetementId(String nom)
    //   public TypeRevetement getTypeRevetement(int id)
    private class TypeRevetement {
        float prixUnitaire;
        String nom;         // UX only
        String description; // UX only

        public TypeRevetement(float eprix, String enom, String edescription) {
            prixUnitaire = eprix;
            nom = enom;
            description = edescription;
        }

        public float getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(float eprix) { prixUnitaire = eprix; }
        public String getNom() { return nom; }
        public void setNom(String enom) { nom = enom; }
        public String getDescription() { return description; }
        public void setDescription(String edescription) { description = edescription; }

        public String toString() {
            return toString(0);
        }
        public String toString(int depth) {
            String pfx = "";
            for (int i = 0; i < depth; i++) {
                pfx += "  ";
            }

            return "TypeRevetement { prixUnitaire: " + prixUnitaire + ", nom: '" + nom + "', description: '" + description + "' }";
        }
    }
    // créer un type de revetement
    public TypeRevetement newTypeRevetement(float eprix, String enom, String edescription, int newId) throws Exception {
        // check if this ID is available
        if (typesRevetement.containsKey(newId))
            throw new Exception("a TypeRevetement with id " + newId + " already exists");

        // create and return the typeRevetement
        TypeRevetement t = new TypeRevetement(eprix, enom, edescription);
        typesRevetement.put(newId, t);
        return t;
    }
    public TypeRevetement newTypeRevetement(float eprix, String enom, String edescription) throws Exception {
        return newTypeRevetement(eprix, enom, edescription, nextId());
    }
    public Integer getTypeRevetementId(String nom) {
        for (int eachId: typesRevetement.keySet()) {
            TypeRevetement t = typesRevetement.get(eachId);
            if (t.nom.equals(nom))
                return eachId;
        }
        return null;
    }
    public TypeRevetement getTypeRevetement(int id) {
        if (typesRevetement.containsKey(id))
            return typesRevetement.get(id);
        else
            return null;
    }

    /// subclass RevetementMur (private) and its stuff
    // describes one 'revetement' of a Mur
    // note: instances of this have need no id as they are attached to a unique Mur as long as they exist
    // utils:
    //   
    private class RevetementMur {
        TypeRevetement type;
        float surface; // TODO tmp

        public RevetementMur(TypeRevetement etype, float esurface) {
            type = etype;
            surface = esurface;
        }
        public String toString() {
            return toString(0);
        }
        public String toString(int depth) {
            String pfx = "";
            for (int i = 0; i < depth; i++) {
                pfx += "  ";
            }
            String pfx2 = ",\n" + pfx + "  ";

            return "RevetementMur { type -> " + type.nom + ", surface: " + surface + " }";
        }
        public String toStringShort() {
            return "( #" + getTypeRevetementId(type.nom) + ", " + surface + ")";
        }
        public float calculatePrice() {
            return surface * type.getPrixUnitaire();
        }
    }
    // add a RevetementMur to an existing Mur
    public RevetementMur addRevetementMur(Mur mur, boolean coteDeux, TypeRevetement type, float surface) {
        // !!! coteDeux == false -> n°1 ;; coteDeux == true -> n°2
        RevetementMur r = new RevetementMur(type, surface);
        mur.addRevetement(r, coteDeux);
        return r;
    }

    /// subclass RevetementSolPlafond (private) and its stuff
    // describes one 'revetement' of a Sol or Mur in a Piece
    // note: instances of this have need no id as they are attached to a unique Mur as long as they exist
    // utils:
    //   
    private class RevetementSolPlafond {
        TypeRevetement type;
        float surface; // TODO tmp

        public RevetementSolPlafond(TypeRevetement etype, float esurface) {
            type = etype;
            surface = esurface;
        }
        public String toString() {
            return toString(0);
        }
        public String toString(int depth) {
            String pfx = "";
            for (int i = 0; i < depth; i++) {
                pfx += "  ";
            }
            String pfx2 = ",\n" + pfx + "  ";

            return "RevetementSolPlafond { type -> " + type.nom + ", surface: " + surface + " }";
        }
        public String toStringShort() {
            return "( #" + getTypeRevetementId(type.nom) + ", " + surface + ")";
        }
        public float calculatePrice() {
            return surface * type.getPrixUnitaire();
        }
    }
    // add a RevetementSolPlafond to an existing Piece
    public RevetementSolPlafond addRevetementSolPlafond(Piece piece, boolean plafond, TypeRevetement type, float surface) {
        // !!! plafond == false -> sol ;; plafond == true -> plafond
        RevetementSolPlafond r = new RevetementSolPlafond(type, surface);
        piece.addRevetement(r, plafond);
        return r;
    }

    /// subclass Piece (private) and its stuff
    // utils:
    private class Piece {
        ArrayList<Mur> murs;
        ArrayList<RevetementSolPlafond> revetementsSol;
        ArrayList<RevetementSolPlafond> revetementsPlafond;
        int niveau; // TODO

        public Piece(ArrayList emurs, int eniveau) {
            murs = emurs;
            niveau = eniveau;
            revetementsSol = new ArrayList<RevetementSolPlafond>();
            revetementsPlafond = new ArrayList<RevetementSolPlafond>();
        }
        public String toString() {
            return toString(0);
        }
        public String toString(int depth) {
            String pfx = "";
            for (int i = 0; i <= depth; i++) {
                pfx += "  ";
            }
            int nextDepth = depth + 1;

            String mursOut = "[ ";
            for (Mur each: murs) {
                mursOut += each.toStringShort() + ", ";
            }
            mursOut += "]";

            String revetementsSolOut = "[ ";
            for (RevetementSolPlafond each: revetementsSol) {
                revetementsSolOut += each.toStringShort() + ", ";
            }
            revetementsSolOut += "]";

            String revetementsPlafondOut = "[ ";
            for (RevetementSolPlafond each: revetementsPlafond) {
                revetementsPlafondOut += each.toStringShort() + ", ";
            }
            revetementsPlafondOut += "]";

            return "Piece {\n"
                + pfx + "murs: " + mursOut + ",\n"
                + pfx + "revetementsSol: " + revetementsSolOut + ",\n"
                + pfx + "revetementsPlafond: " + revetementsPlafondOut + ",\n"
                + pfx + "niveau: " + niveau + ",\n"
                + pfx + "}";
        }
        public void addRevetement(RevetementSolPlafond r, boolean plafond) {
            if (plafond)
                revetementsPlafond.add(r);
            else
                revetementsSol.add(r);
        }
    }
}
