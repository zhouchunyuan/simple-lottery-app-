import sys
import random,math
import time,simpleaudio as sa

if sys.version_info[0] == 2:  # Just checking your Python version to import Tkinter properly.
    from Tkinter import *
    import thread
else:
    from tkinter import *
    import _thread as thread


class Fullscreen_Window:

    TOTAL = 5
    heartSnd = sa.WaveObject.from_wave_file("heart.wav")
    CoinSnd = sa.WaveObject.from_wave_file("Pickup_Coin.wav")
    playHeartSnd = None
    playCoinSnd = None
    def testVal(inStr):
        try:
            n = int(inStr)
        except:
            return False
        return True
    def input(self):
        self.frame0 = Frame(self.tk)
        self.frame0.pack()
        self.l=Label(self.frame0,text="请输入人数：").pack()
        self.e=Entry(self.frame0,validatecommand = (self.tk.register(self.testVal),'%P'))
        self.e.pack()
        self.b=Button(self.frame0,text='确认',command=self.cleanup)
        self.b.pack()
    def cleanup(self):
        try:
            self.TOTAL = int(self.e.get())
            self.frame0.destroy()
        except:
            pass

        
    def __init__(self):

        self.tk = Tk()
        self.tk.title("2017尼康论坛抽奖程序")
        self.input()
        self.running = True
        self.was_running = True
        self.numList = []
        
        self.tk.bind("<Escape>", self.toggle_fullscreen)
        self.tk.bind("<space>", self.pause)
        self.state = True
        self.tk.attributes("-fullscreen", True)

        self.initUI()
        self.update()
        
      
    def initUI(self):

        self.phase = 0
        self.lnumber = Label(text="")
        
        frame = Frame(self.tk)
        frame.pack(side=LEFT,fill=BOTH)
        self.llist = Label(frame,fg='red',font=("宋体", 20),text="")
        self.llist.pack(side=TOP)

        self.lnumber.pack(fill=BOTH, expand=1)

        

    def toggle_fullscreen(self, event=None):
        self.state = not self.state  # Just toggling the boolean
        self.tk.attributes("-fullscreen", self.state)
        return "break"

    def pause(self,event=None):
        if not self.running :
            self.running = True
        else:
            self.phase = 0
            self.running= False
            self.was_running = True
            
    
    def update(self):
        """Runs every 100ms """

        self.tk.after(10,self.update)
        
        self.phase += 1
        endPhase = 100
        self.phase %= endPhase

        if len(self.numList)>=self.TOTAL:
            self.tk.destroy()
            
        if self.running :
            if self.playHeartSnd is None:
                self.playHeartSnd = self.heartSnd.play()
            elif not self.playHeartSnd.is_playing():
                self.playHeartSnd = None

            size = int(200 + 100*math.sin(6.28*self.phase/80))
            number = random.randint(1,self.TOTAL)
            while number in self.numList:
                number = random.randint(1,self.TOTAL)
                
            self.lnumber.config(font=("Courier", size),
                                fg=random.choice(['black','yellow','orange','purple','green']),
                                text=str(number))
        elif self.was_running :
            if self.playCoinSnd is None:
                self.playCoinSnd = self.CoinSnd.play()
            elif not self.playCoinSnd.is_playing():
                self.playCoinSnd = None
            

            number = random.randint(1,self.TOTAL)
            while number in self.numList:
                number = random.randint(1,self.TOTAL)
            self.lnumber.config(font=("Courier", 300),fg='red',text=str(number))
            if self.phase > endPhase-2 :
                number = int(self.lnumber['text'])
                self.numList.append(number)
                msg="中奖号码："
                for i,n in enumerate(self.numList):
                    msg += "\n ("+str(i+1)+") "+str(n)
                self.llist.config(text=str(msg))
                
                self.was_running = False

if __name__ == '__main__':
##    p = popupWindow()
##    p.tk.mainloop()

    w = Fullscreen_Window()
    w.tk.mainloop()

