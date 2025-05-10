import tkinter as tk
from tkinter import ttk
import customtkinter as ctk



colour1 = '#36454f'
colour2 = '#536872'
colour3 = '#47555c'
colour4 = '#2a343b'


def updateprogress(value):
    global root
    global progressbar
    if(value == 0):
        progressbar.configure(progress_color=colour4)
    else:
        progressbar.configure(progress_color="#80ef80")
    progressbar.set(value/100)  # Set the progress bar value
    root.update_idletasks()


def starting():
    global progresslabel
    clearerrormessage()
    updateprogress(0)
    progresslabel.configure(text="Starting...")


def finished():
    global progresslabel
    updateprogress(0)
    progresslabel.configure(text="Finished")

def getautochoice():
    global autoswitch
    return autoswitch.get()

def createerrormessage(message):
    global errormsg
    errormsg.configure(text="Error: " + message)

def clearerrormessage():
    global errormsg
    errormsg.configure(text="")


def GUI():
    global root
    global progressbar
    global progresslabel
    global errormsg
    global autoswitch
    
    root = ctk.CTk()
    root.title("SudokuSolver")
    root.geometry("400x300")
    root.resizable(False, False)
    root.configure(fg_color=colour1)

    frame = ctk.CTkFrame(root, fg_color=colour1, border_width=8, border_color=colour4, width=380, height=270)
    frame.place(x=10,y=20)

    ctk.CTkLabel(root, text="Sudoku Solver", font=("Helvetica", 26), text_color = '#FFFFFF', width=170, height=50).place(x=115, y=0)

    ctk.CTkLabel(frame, text="Place Mouse in the top-left sudoku box \nPress shift+n to start - keep the box selected",
             font=("Helvetica", 14), text_color='#FFFFFF', width=340).place(x=20,y=50)
    


    autoswitch = ctk.CTkSwitch(frame, text="Auto", font=("Helvetica", 14), text_color='#FFFFFF', border_width=1)
    autoswitch.place(relx=0.55, y=130, anchor="center")


    progresslabel = ctk.CTkLabel(frame, text="", text_color='#FFFFFF', font=("Helvetica", 12))
    progresslabel.place(x=60,y=150)

    progressbar = ctk.CTkProgressBar(frame, width=280, height=30, progress_color="#80ef80", fg_color=colour4)
    progressbar.place(x=50, y=180)
    progressbar.set(0)
    progressbar.configure(progress_color=colour4)

    errormsg = ctk.CTkLabel(frame, text="", text_color="#FF746c", font=("Helvetica", 12))
    errormsg.place(x=60, y=220)


    root.mainloop()
