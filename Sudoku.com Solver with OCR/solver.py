import copy
import time

def StartSolver(inp):
    start_time = time.time()
    
    startArray = BuildInitialArray(inp)
    solved = SolveSudoku(startArray)

    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Sudoku solved in {elapsed_time:.6f} seconds")
    
    return arrayToString(solved)

def SolveSudoku(array):
    global changes
    changes = True
    while(changes):
        changes = False
        noteArray = makeNotes(array)

        noteArray = doNakedPairs(array, noteArray)
        noteArray = doPointingPairs(array, noteArray)
        noteArray = doHiddenPairs(array, noteArray)
        noteArray = doXWing(array, noteArray)

        array = fillSingleValues(array, noteArray)
        array = fillHiddenSingles(array, noteArray)

    
    if(issolved(array) == False):
        #Do guesses
        original = copy.deepcopy(array)
        array = backtracking(array, original)
    return array



def issolved(arr):
    for i in range(0,9):
        for j in range(0,9):
            if(arr[i][j] == "0"):
                return False
    return True


def fillSingleValues(array, noteArray):
    global changes
    for i in range(9):
        for j in range(9):
            if len(noteArray[i][j]) == 1 and isNumberPossible(array, int(noteArray[i][j]), i, j):
                array[i][j] = noteArray[i][j]
                changes = True
    return array


def fillHiddenSingles(array, noteArray):
    global changes
    for n in range(1, 10):
        num = str(n)

        # Check each row
        for i in range(9):
            count = 0
            col = -1
            for j in range(9):
                if num in noteArray[i][j]:
                    count += 1
                    col = j
            if count == 1:
                if isNumberPossible(array, int(num), i, col):
                    array[i][col] = num
                    changes = True

        # Check each column
        for j in range(9):
            count = 0
            row = -1
            for i in range(9):
                if num in noteArray[i][j]:
                    count += 1
                    row = i
            if count == 1:
                if isNumberPossible(array, int(num), row, j):
                    array[row][j] = num
                    changes = True

        # Check boxes
        for q in range(9):
            count = 0
            row = -1
            column = -1
            for i in range((q // 3) * 3, ((q // 3) * 3) + 3):
                for j in range((q % 3) * 3, ((q % 3) * 3) + 3):
                    if num in noteArray[i][j]:
                        count += 1
                        row = i
                        column = j
            if count == 1:
                if isNumberPossible(array, int(num), row, column):
                    array[row][column] = num
                    changes = True
    return array

def doNakedPairs(array, noteArray):
    # Check each row
    for i in range(9):
        possiblePairs = ""
        pair = ""
        for j in range(9):
            if len(noteArray[i][j]) == 2:
                if possiblePairs != "":
                    if noteArray[i][j] in possiblePairs:
                        pair = noteArray[i][j]
                        possiblePairs += noteArray[i][j]
                else:
                    possiblePairs += noteArray[i][j]
        reversePair = pair[::-1]
        if pair != "":
            for j in range(9):
                if str(pair[0]) in noteArray[i][j] and noteArray[i][j] != pair and noteArray[i][j] != reversePair:
                    noteArray[i][j] = noteArray[i][j].replace(str(pair[0]), "")
                if str(pair[1]) in noteArray[i][j] and noteArray[i][j] != pair and noteArray[i][j] != reversePair:
                    noteArray[i][j] = noteArray[i][j].replace(str(pair[1]), "")

    # For each column
    for j in range(9):
        possiblePairs = ""
        pair = ""
        for i in range(9):
            if len(noteArray[i][j]) == 2:
                if possiblePairs != "":
                    if noteArray[i][j] in possiblePairs:
                        pair = noteArray[i][j]
                        possiblePairs += noteArray[i][j]
                else:
                    possiblePairs += noteArray[i][j]
        reversePair = pair[::-1]
        if pair != "":
            for i in range(9):
                if str(pair[0]) in noteArray[i][j] and noteArray[i][j] != pair and noteArray[i][j] != reversePair:
                    noteArray[i][j] = noteArray[i][j].replace(str(pair[0]), "")
                if str(pair[1]) in noteArray[i][j] and noteArray[i][j] != pair and noteArray[i][j] != reversePair:
                    noteArray[i][j] = noteArray[i][j].replace(str(pair[1]), "")

    # For each quadrant
    for q in range(9):
        possiblePairs = ""
        pair = ""
        for i in range((q // 3) * 3, ((q // 3) * 3) + 3):
            for j in range((q % 3) * 3, ((q % 3) * 3) + 3):
                if len(noteArray[i][j]) == 2:
                    if possiblePairs != "":
                        if noteArray[i][j] in possiblePairs:
                            pair = noteArray[i][j]
                            possiblePairs += noteArray[i][j]
                    else:
                        possiblePairs += noteArray[i][j]
        reversePair = pair[::-1]
        if pair != "":
            for i in range((q // 3) * 3, ((q // 3) * 3) + 3):
                for j in range((q % 3) * 3, ((q % 3) * 3) + 3):
                    if str(pair[0]) in noteArray[i][j] and noteArray[i][j] != pair and noteArray[i][j] != reversePair:
                        noteArray[i][j] = noteArray[i][j].replace(str(pair[0]), "")
                    if str(pair[1]) in noteArray[i][j] and noteArray[i][j] != pair and noteArray[i][j] != reversePair:
                        noteArray[i][j] = noteArray[i][j].replace(str(pair[1]), "")
    return noteArray

def doHiddenPairs(array, noteArray):
    for q in range(9):
        rowStart = (q // 3) * 3
        colStart = (q % 3) * 3
        candidates = ""
        for n in range(1, 10):
            num = str(n)
            count = 0
            for i in range(rowStart, rowStart + 3):
                for j in range(colStart, colStart + 3):
                    if num in noteArray[i][j]:
                        count += 1
            if count == 2:
                candidates += num

        coords = [[-1, -1], [-1, -1]]
        pair1 = '0'
        pair2 = '0'
        found = False

        for k in range(len(candidates)):
            for c in range(len(candidates)):
                count = 0
                for i in range(rowStart, rowStart + 3):
                    for j in range(colStart, colStart + 3):
                        if c != k:
                            if candidates[k] in noteArray[i][j] and candidates[c] in noteArray[i][j]:
                                coords[count][0] = i
                                coords[count][1] = j
                                count += 1
                if count == 2:
                    found = True
                    pair1 = candidates[k]
                    pair2 = candidates[c]
                    break
            if found:
                break

        if found:
            for k in range(2):
                notes = noteArray[coords[k][0]][coords[k][1]]
                amount = len(notes)
                if amount > 2:
                    for c in range(amount):
                        if notes[c] != pair1 and notes[c] != pair2:
                            character = notes[c]
                            noteArray[coords[k][0]][coords[k][1]] = noteArray[coords[k][0]][coords[k][1]].replace(character, "")
    return noteArray

def doPointingPairs(array, noteArray):
    for n in range(1, 10):
        num = str(n)
        for q in range(9):
            found = False
            row = -1
            for i in range((q // 3) * 3, ((q // 3) * 3) + 3):
                for j in range((q % 3) * 3, ((q % 3) * 3) + 3):
                    if num in noteArray[i][j]:
                        if row == -1:
                            row = i
                        elif row == i:
                            found = True
                        else:
                            found = False
                            break
                if not found:
                    break

            if found and row != -1:
                dontCheck1 = (q % 3) * 3
                dontCheck2 = ((q % 3) * 3) + 1
                dontCheck3 = ((q % 3) * 3) + 2
                for j in range(9):
                    if num in noteArray[row][j] and j != dontCheck1 and j != dontCheck2 and j != dontCheck3:
                        noteArray[row][j] = noteArray[row][j].replace(num, "")

            else:
                found = False
                column = -1
                for i in range((q // 3) * 3, ((q // 3) * 3) + 3):
                    for j in range((q % 3) * 3, ((q % 3) * 3) + 3):
                        if num in noteArray[i][j]:
                            if column == -1:
                                column = j
                            elif column == j:
                                found = True
                            else:
                                found = False
                                break
                    if not found:
                        break

                if found and column != -1:
                    dontCheck1 = (q // 3) * 3
                    dontCheck2 = ((q // 3) * 3) + 1
                    dontCheck3 = ((q // 3) * 3) + 2
                    for i in range(9):
                        if num in noteArray[i][column] and i != dontCheck1 and i != dontCheck2 and i != dontCheck3:
                            noteArray[i][column] = noteArray[i][column].replace(num, "")
    return noteArray




def doXWing(array, noteArray):
    for n in range(1, 10):
        num = str(n)
        rows = [[-1, -1], [-1, -1]]
        cols = [-1, -1]
        colCount = 0
        for j in range(9):
            numCount = 0
            for i in range(9):
                if num in noteArray[i][j]:
                    if numCount < 2 and colCount < 2:
                        rows[colCount][numCount] = i
                    numCount += 1
            if numCount == 2:
                if colCount < 2:
                    cols[colCount] = j
                colCount += 1
        if colCount == 2:
            if rows[0][0] == rows[1][0] and rows[0][1] == rows[1][1]:
                for k in range(2):
                    row = rows[0][k]
                    for j in range(9):
                        if num in noteArray[row][j] and j != cols[0] and j != cols[1]:
                            noteArray[row][j] = noteArray[row][j].replace(num, "")

    for n in range(1, 10):
        num = str(n)
        cols = [[-1, -1], [-1, -1]]
        rows = [-1, -1]
        rowCount = 0
        for i in range(9):
            numCount = 0
            for j in range(9):
                if num in noteArray[i][j]:
                    if numCount < 2 and rowCount < 2:
                        cols[rowCount][numCount] = j
                    numCount += 1
            if numCount == 2:
                if rowCount < 2:
                    rows[rowCount] = i
                rowCount += 1
        if rowCount == 2:
            if cols[0][0] == cols[1][0] and cols[0][1] == cols[1][1]:
                for k in range(2):
                    col = cols[0][k]
                    for i in range(9):
                        if num in noteArray[i][col] and i != rows[0] and i != rows[1]:
                            noteArray[i][col] = noteArray[i][col].replace(num, "")
    return noteArray






def backtracking(arr, original):
    row = 0
    col = 0

    direction = 1 #Left 0 or right 1

    solved = False
    while solved == False:


        #Make sure the original cell is not already filled in before guessing
        if(original[row][col] == "0"):
            
            current = int(arr[row][col])
            if(current == 9):
                arr[row][col] = "0"
                row,col = goback(row,col)
                direction = 0
            else:
                arr[row][col] = "0"
                #The numbers to guess 1-9
                for n in range(current+1,10):
                    #Check the guess is possible
                    if(isNumberPossible(arr,n,row,col)):
                        arr[row][col] = str(n)
                        if(row==8 and col==8):
                            return arr
                        row,col = gonext(row,col)
                        direction = 1
                        break
                    #If all numbers arent possible then backtrack
                    elif(n==9):
                        arr[row][col] = "0"
                        row,col = goback(row,col)
                        direction = 0
                    
        else:
            if(row==8 and col==8):
                return arr
            elif(direction==0):
                row,col = goback(row,col)
            elif(direction==1):
                row,col = gonext(row,col)

    return 0



def goback(row, col):

    if(col==0 and row==0):
        return (row,col)
    elif(col==0):
        return (row-1, 8)
    else:
        return (row, col-1)


def gonext(row, col):

    if(col==8 and row==8):
        return (row,col)
    elif(col==8):
        return (row+1, 0)
    else:
        return (row, col+1)





def BuildInitialArray(initial):
    startArray = [["0" for _ in range(9)] for _ in range(9)]
    stringIndex = 0
    for i in range(9):
        for j in range(9):
            startArray[i][j] = initial[stringIndex]
            stringIndex += 1
    return startArray

def outputArray(array):
    for i in range(9):
        output = ""
        for j in range(9):
            output += array[i][j] + ","
        print(output)

def outputArrayNice(array):
    maxLength = 0
    for i in range(9):
        for j in range(9):
            if len(array[i][j]) > maxLength:
                maxLength = len(array[i][j])
    for i in range(9):
        for j in range(9):
            length = len(array[i][j])
            addedChars = maxLength - length
            if addedChars > 0:
                array[i][j] += "0" * addedChars
    outputArray(array)

def makeNotes(array):
    noteArray = [["" for _ in range(9)] for _ in range(9)]
    for i in range(9):
        for j in range(9):
            if array[i][j] == "0":
                for n in range(1, 10):
                    if isNumberPossible(array, n, i, j):
                        noteArray[i][j] += str(n)
    return noteArray


def arrayToString(array):
    output = ""
    for i in range(9):
        for j in range(9):
            output += array[i][j]
    return output

def getBoxStart(i, j):
    quadrant = (i // 3) * 3 + (j // 3)
    return [quadrant // 3, quadrant * 3]

def isNumberPossible(array, number, i, j):
    if array[i][j] != "0":
        return False
    q = (i // 3) * 3 + (j // 3)
    rowStart = (q // 3) * 3
    colStart = (q % 3) * 3
    for row in range(rowStart, rowStart + 3):
        for col in range(colStart, colStart + 3):
            if array[row][col] == str(number):
                return False
    for k in range(9):
        if k != j and array[i][k] == str(number):
            return False
    for k in range(9):
        if k != i and array[k][j] == str(number):
            return False
    return True

# Global variables
changes = False
