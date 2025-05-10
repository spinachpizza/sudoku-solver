public class SudokuSolver {

    public static String[][] startArray;

    public static boolean changes = true;

    public static GUI gui;


    public static void main(String[] args) {

        gui = new GUI();

        /* 
        String[][] noteArray = new String[9][9];
        // Initialize the noteArray with empty strings 
        for (int i = 0; i < noteArray.length; i++) { 
            for (int j = 0; j < noteArray[i].length; j++) { 
                if (noteArray[i][j] == null) { noteArray[i][j] = ""; } 
            } 
        }
        noteArray[7][4] = "4";
        noteArray[7][1] = "4";
        noteArray[5][1] = "4";
        noteArray[5][4] = "4";
        noteArray[7][7] = "4";
        noteArray[2][4] = "4";
        
        outputArrayNice(noteArray);
        String[][] solved = doXWing(startArray, noteArray);
        System.out.println("-----");
        outputArrayNice(solved);
        */

        //String input = "017050900000000001200000400000031560900600040008000090046080000003400000080006000";
        //StartSolver(input);
    }

    public static void StartSolver(String input) {
        System.out.println(input);
        startArray = new String[9][9];

        BuildInitialArray(input);
        long start = System.currentTimeMillis();
        String[][] solved = SolveSudoku(startArray);
        long time = System.currentTimeMillis() - start;
        //outputArray(solved);
        gui.showOutputWindow(solved);
        System.out.println("Time taken: " + (time / 1000.0) + " seconds " + "(" + time + "ms)");
    }


    public static String[][] SolveSudoku(String[][] array) {

        changes = true;
        while(changes == true) {
            changes = false;
            String[][] noteArray = makeNotes(array);

            noteArray = doNakedPairs(array, noteArray);
            noteArray = doPointingPairs(array, noteArray);
            noteArray = doHiddenPairs(array, noteArray);
            noteArray = doXWing(array, noteArray);
            //outputArrayNice(noteArray);
            //System.out.println("========================");
            array = fillSingleValues(array, noteArray);
            array = fillHiddenSingles(array, noteArray);
        }

        return array;
    }


    public static String[][] fillSingleValues(String[][] array, String[][] noteArray) {
        
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(noteArray[i][j].length() == 1 && isNumberPossible(array, Integer.parseInt(noteArray[i][j]), i, j)) {
                    array[i][j] = noteArray[i][j];
                    changes = true;
                }
            }
        }

        return array;
    }



    public static String[][] fillHiddenSingles(String[][] array, String[][] noteArray) {

        for(int n=1; n<10; n++) {
            String num = Integer.toString(n);

            //Check each row
            for(int i=0; i<9; i++) {
                int count = 0;
                int col = -1;
                for(int j=0; j<9; j++) {
                    if(noteArray[i][j].contains(num)) {
                        count++;
                        col = j;
                    }
                }
                if(count == 1) {
                    if(isNumberPossible(array, Integer.parseInt(num), i, col)) {
                        array[i][col] = num;
                        changes = true;
                    }
                }
            }

            //Check each column
            for(int j=0; j<9; j++) {
                int count = 0;
                int row = -1;
                for(int i=0; i<9; i++) {
                    if(noteArray[i][j].contains(num)) {
                        count++;
                        row = i;
                    }
                }
                if(count == 1) {
                    if(isNumberPossible(array, Integer.parseInt(num), row, j)) {
                        array[row][j] = num;
                        changes = true;
                    }
                }
            }

            //Check boxes
            for(int q=0; q<9; q++) {

                int count = 0;
                int row = -1;
                int column = -1;


                for(int i=(q/3)*3; i<((q/3)*3)+3; i++) {
                    for(int j=(q%3)*3; j<((q%3)*3)+3; j++) {

                        if(noteArray[i][j].contains(num)) {
                            count++;
                            row=i;
                            column=j;
                        }
                    }
                }
                if(count==1) {
                    if(isNumberPossible(array, Integer.parseInt(num), row, column)) {
                        array[row][column] = num;
                        changes = true;
                    }
                }
            }


        }

        return array;
    }


    public static String[][] doNakedPairs(String[][] array, String[][] noteArray) {

        //Check each row
        for(int i=0; i<9; i++) {

            String possiblePairs = "";
            String pair = "";

            for(int j=0; j<9; j++) {

                if(noteArray[i][j].length() == 2) {
                    
                    if(!possiblePairs.equals("")) {
                        //Check it matches
                        if(possiblePairs.contains(noteArray[i][j])) {
                            pair = noteArray[i][j];
                            possiblePairs = possiblePairs + noteArray[i][j];
                        }
                    } else {
                        possiblePairs = possiblePairs + noteArray[i][j];
                    }
                }
            }

            String reversePair = new StringBuilder(pair).reverse().toString();
            
            //Remove those from other notes if found
            if(!pair.equals("")) {
                for(int j=0; j<9; j++) {
                    //Check for both numbers in the pair found if they are in the cell
                    if(noteArray[i][j].contains(String.valueOf(pair.charAt(0))) && !noteArray[i][j].equals(pair) && !noteArray[i][j].equals(reversePair)) {
                        noteArray[i][j] = noteArray[i][j].replace(String.valueOf(pair.charAt(0)), "");
                    }
                    if(noteArray[i][j].contains(String.valueOf(pair.charAt(1))) && !noteArray[i][j].equals(pair) && !noteArray[i][j].equals(reversePair)) {
                        noteArray[i][j] = noteArray[i][j].replace(String.valueOf(pair.charAt(1)), "");
                    }
                }
            }
        }


        //For each column
        for(int j=0; j<9; j++) {

            String possiblePairs = "";
            String pair = "";

            for(int i=0; i<9; i++) {

                if(noteArray[i][j].length() == 2) {
                    
                    if(!possiblePairs.equals("")) {
                        //Check it matches
                        if(possiblePairs.contains(noteArray[i][j])) {
                            pair = noteArray[i][j];
                            possiblePairs = possiblePairs + noteArray[i][j];
                        }
                    } else {
                        possiblePairs = possiblePairs + noteArray[i][j];
                    }
                }
            }

            String reversePair = new StringBuilder(pair).reverse().toString();
            
            //Remove those from other notes if found
            if(!pair.equals("")) {
                for(int i=0; i<9; i++) {
                    //Check for both numbers in the pair found if they are in the cell
                    if(noteArray[i][j].contains(String.valueOf(pair.charAt(0))) && !noteArray[i][j].equals(pair) && !noteArray[i][j].equals(reversePair)) {
                        noteArray[i][j] = noteArray[i][j].replace(String.valueOf(pair.charAt(0)), "");    
                    }
                    if(noteArray[i][j].contains(String.valueOf(pair.charAt(1))) && !noteArray[i][j].equals(pair) && !noteArray[i][j].equals(reversePair)) {
                        noteArray[i][j] = noteArray[i][j].replace(String.valueOf(pair.charAt(1)), "");
                    }
                }
            }
        }



        //For each quadrant
        for(int q=0; q<9; q++) {

            String possiblePairs = "";
            String pair = "";


            for(int i=(q/3)*3; i<((q/3)*3)+3; i++) {
                for(int j=(q%3)*3; j<((q%3)*3)+3; j++) {

                    if(noteArray[i][j].length() == 2) {
                    
                        if(!possiblePairs.equals("")) {
                            //Check it matches
                            if(possiblePairs.contains(noteArray[i][j])) {
                                pair = noteArray[i][j];
                                possiblePairs = possiblePairs + noteArray[i][j];
                            }
                        } else {
                            possiblePairs = possiblePairs + noteArray[i][j];
                        }
                    }
                }
            }

            String reversePair = new StringBuilder(pair).reverse().toString();

            if(!pair.equals("")) {
                for(int i=(q/3)*3; i<((q/3)*3)+3; i++) {
                    for(int j=(q%3)*3; j<((q%3)*3)+3; j++) {
                        //Check for both numbers in the pair found if they are in the cell
                        if(noteArray[i][j].contains(String.valueOf(pair.charAt(0))) && !noteArray[i][j].equals(pair) && !noteArray[i][j].equals(reversePair)) {
                            noteArray[i][j] = noteArray[i][j].replace(String.valueOf(pair.charAt(0)), "");
                        }
                        if(noteArray[i][j].contains(String.valueOf(pair.charAt(1))) && !noteArray[i][j].equals(pair) && !noteArray[i][j].equals(reversePair)) {
                            noteArray[i][j] = noteArray[i][j].replace(String.valueOf(pair.charAt(1)), "");
                        }
                    }
                }
            }

        }

        return noteArray;
    }


    public static String[][] doHiddenPairs(String[][] array, String[][] noteArray) {

        for(int q=0; q<9; q++) {

            //First identify numbers that appear only twice
            int rowStart = (q/3)*3;
            int colStart = (q%3)*3;
            String candidates = "";
            for(int n=1; n<10; n++) {

                String num = String.valueOf(n);

                int count = 0;
                for(int i=rowStart; i<rowStart+3; i++) {
                    for(int j=colStart; j<colStart+3; j++) {
                        if(noteArray[i][j].contains(num)) {
                            count++;
                        }
                    }
                }
                if(count==2) {
                    candidates = candidates + num;
                }
            }

            //Then check if they are a pair
            int[][] coords = new int[2][2];
            char pair1 = '0';
            char pair2 = '0';
            boolean found = false;

            outerLoop:
            for(int k=0; k<candidates.length(); k++) { //Loop through each candidate
                    
                for(int c=0; c<candidates.length(); c++) {
                    int count = 0; //Check for pair
                    for(int i=rowStart; i<rowStart+3; i++) {
                        for(int j=colStart; j<colStart+3; j++) {
                    
                            if(c!=k) {
                                if(noteArray[i][j].contains(String.valueOf(candidates.charAt(k))) && noteArray[i][j].contains(String.valueOf(candidates.charAt(c)))) {
                                    coords[count][0] = i;
                                    coords[count][1] = j;
                                    count++;
                                }
                            }
                        }
                    }
                    if(count==2) {
                        found = true;
                        pair1 = candidates.charAt(k);
                        pair2 = candidates.charAt(c);
                        //System.out.println("Hidden pair found ("+pair1+","+pair2+") in quadrant: " + q + " with coords: (" +coords[0][0] + "," + coords[0][1] + ") (" + coords[1][0] +","+coords[1][1]+")");
                        break outerLoop;
                    }
                }
            }

            if(found==true) {
                for(int k=0; k<2; k++) {
                    String notes = noteArray[coords[k][0]][coords[k][1]];
                    int amount = notes.length();
                    if(amount > 2) {
                        for(int c=0; c<amount; c++) {
                            if(notes.charAt(c) != pair1 && notes.charAt(c) != pair2) {
                                String character = String.valueOf(notes.charAt(c));
                                noteArray[coords[k][0]][coords[k][1]] = noteArray[coords[k][0]][coords[k][1]].replace(character, "");
                            }
                        }
                    }
                }
            }
        }

        return noteArray;
    }


    public static String[][] doPointingPairs(String[][] array, String[][] noteArray) {

        
        for(int n=1; n<10; n++) {

            String num = String.valueOf(n);

            for(int q=0; q<9; q++) {

                //Check horizontal
                boolean found = false;
                int row = -1;

                outerLoop:
                for(int i=(q/3)*3; i<((q/3)*3)+3; i++) {
                    for(int j=(q%3)*3; j<((q%3)*3)+3; j++) {

                        if(noteArray[i][j].contains(num)) {
                            if(row == -1) {
                                row = i;
                            }else if(row == i) {
                                found = true;
                            } else {
                                found = false;
                                break outerLoop;
                            }
                        }

                    }
                }

                if(found && row!= -1) {
                    //Column coordinates of the pointing pair / triplet
                    int dontCheck1 = ((q%3)*3);
                    int dontCheck2 = ((q%3)*3) + 1;
                    int dontCheck3 = ((q%3)*3) + 2;
                    //Loop through the row and remove any affected by pointing pairs
                    for(int j=0; j<9; j++) {
                        if(noteArray[row][j].contains(num) && j!=dontCheck1 && j!= dontCheck2 && j!=dontCheck3) {
                            noteArray[row][j] = noteArray[row][j].replace(num,"");
                        }
                    }

                //Check vertical (not possible to be horizontal and vetical pointing pair)
                } else {

                    found = false;
                    int column = -1;

                    outerLoop:
                    for(int i=(q/3)*3; i<((q/3)*3)+3; i++) {
                        for(int j=(q%3)*3; j<((q%3)*3)+3; j++) {

                            if(noteArray[i][j].contains(num)) {
                                if(column == -1) {
                                    column = j;
                                }else if(column == j) {
                                    found = true;
                                } else {
                                    found = false;
                                    break outerLoop;
                                }
                            }

                        }
                    }

                    if(found && column!=-1) {
                        //Column coordinates of the pointing pair / triplet
                        int dontCheck1 = ((q/3)*3);
                        int dontCheck2 = ((q/3)*3) + 1;
                        int dontCheck3 = ((q/3)*3) + 2;
                        //Loop through the column and remove any affected by pointing pairs
                        for(int i=0; i<9; i++) {
                            if(noteArray[i][column].contains(num) && i!=dontCheck1 && i!= dontCheck2 && i!=dontCheck3) {
                                noteArray[i][column] = noteArray[i][column].replace(num,"");
                            }
                        }
                    }
                }
            }
        }

        return noteArray;
    }


    public static String[][] doXWing(String[][] array, String[][] noteArray) {

        for(int n=1; n<10; n++) {

            String num = String.valueOf(n);

            //For horizontal elimination (check columns)
            int[][] rows = new int[2][2];
            int[] cols = new int[2];
            int colCount = 0; //Track the amount of columns with 2

            for(int j=0; j<9; j++) {

                int numCount = 0; //Track times it shows up in a column

                for(int i=0; i<9; i++) {
                    if(noteArray[i][j].contains(num)) {
                        if(numCount < 2 && colCount < 2) {
                            rows[colCount][numCount] = i;
                        }
                        numCount++;
                    }
                }

                if(numCount == 2) {
                    if(colCount < 2) {
                        cols[colCount] = j;
                    }
                    colCount++;
                }
            }

            if(colCount == 2) {

                //Check they are actually X wing
                if(rows[0][0] == rows[1][0] && rows[0][1] == rows[1][1]) {

                    //Check both horizontals (rows) for numbers
                    for(int k=0; k<2; k++) {
                        int row = rows[0][k];
                        for(int j=0; j<9; j++) {
                            if(noteArray[row][j].contains(num) && j!=cols[0] && j!=cols[1]) {
                                noteArray[row][j] = noteArray[row][j].replace(num,"");
                            }
                        }
                    }
                }
            }
        }

        for(int n=1; n<10; n++) {

            String num = String.valueOf(n);

            //Check vertical elimination (check rows)
            int[][] cols = new int[2][2];
            int[] rows = new int[2];
            int rowCount = 0;
            
            for(int i=0; i<9; i++) {
                int numCount = 0;
                for(int j=0; j<9; j++) {
                    if(noteArray[i][j].contains(num)) {
                        if(numCount < 2 && rowCount < 2) {
                            cols[rowCount][numCount] = j;
                        }
                        numCount ++;
                    }
                }
                if(numCount == 2) {
                    if(rowCount < 2) {
                        rows[rowCount] = i;
                    }
                    rowCount++;
                }
            }

            if(rowCount == 2) {

                //Check its actually an X wing
                if(cols[0][0] == cols[1][0] && cols[0][1] == cols[1][1]) {

                    //Check both columns for numbers
                    for(int k=0; k<2; k++) {
                        int col = cols[0][k];
                        for(int i=0; i<9; i++) {
                            if(noteArray[i][col].contains(num) && i!=rows[0] && i!=rows[1]) {
                                noteArray[i][col] = noteArray[i][col].replace(num,"");
                            }
                        }
                    }
                }
            }

        }

        return noteArray;
    }



    public static void BuildInitialArray(String initial) {

        int stringIndex = 0;
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                startArray[i][j] = String.valueOf(initial.charAt(stringIndex));
                stringIndex++;
            }
        }
    }


    public static void outputArray(String[][] array) {

        for(int i=0; i<9; i++) {
            String output = "";
            for(int j=0; j<9; j++) {
                output = output + array[i][j] + ",";
            }
            System.out.println(output);
        }
    }


    public static void outputArrayNice(String[][] array) {

        int maxLength = 0;
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(array[i][j].length() > maxLength) {
                    maxLength = array[i][j].length();
                }
            }
        }

        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {

                int length = array[i][j].length();
                int addedChars = maxLength - length;
                if(addedChars > 0) {
                    for(int c=0; c< addedChars; c++) {
                        array[i][j] = array[i][j] + "0";
                    }
                }
            }
        }

        outputArray(array);
    }





    public static String[][] makeNotes(String[][] array) {

        String[][] noteArray = new String[9][9];
        // Initialize the noteArray with empty strings 
        for (int i = 0; i < noteArray.length; i++) { 
            for (int j = 0; j < noteArray[i].length; j++) { 
                if (noteArray[i][j] == null) { noteArray[i][j] = ""; } 
            } 
        }

        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {

                if(array[i][j].equals("0")) {
                    for(int n=1; n<10; n++) {
                        if(isNumberPossible(array, n, i, j)) {
                            noteArray[i][j] = noteArray[i][j] + String.valueOf(n);
                        }
                    }
                }
            }
        }

        return noteArray;
    }





    public static int[] getBoxStart(int i, int j) {

        int quadrant = ((i/3) *3) + (j/3);
        return new int[]{quadrant/3,quadrant*3};    
    }



    public static boolean isNumberPossible(String[][] array, int number, int i, int j) {

        if(!array[i][j].equals("0")) {
            return false;
        }

        //Check if number is in quadrant already
        int q = ((i/3) * 3) + (j/3);
        
        int rowStart = (q/3)*3;
        int colStart = (q%3)*3;
        for(int row=rowStart; row<rowStart+3; row++) {
            for(int col=colStart; col<colStart+3; col++) {
                if(array[row][col].equals(String.valueOf(number))) {
                    return false;
                }
            }
        }
        
        
        //Now check horizontal
        for(int k=0; k<9; k++) {
            if(k!=j) { //Skip over current
                if(array[i][k].equals(String.valueOf(number))) {
                    return false;
                }
            }
        }

        //Now check vertical
        for(int k=0; k<9; k++) {
            if(k!=i) { //Skip over current
                if(array[k][j].equals(String.valueOf(number))) {
                    return false;
                }
            }
        }

        return true;
    }
}
