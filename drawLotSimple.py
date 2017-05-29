#!/usr/bin/python
# -*- coding: utf-8 -*-

"""
modified from ZetCode PyQt4 tutorial 

This is a lottery number generator.

author: Chunyuan Zhou
last edited: May 2017
"""

import sys, random
from PyQt4 import QtCore, QtGui
from PyQt4.QtGui import *


class Lottery(QtGui.QMainWindow):
    
    def __init__(self):
        super(Lottery, self).__init__()
        
        self.initUI()
        
        
        
    def initUI(self):

        self.tboard = Board(self)
        self.setCentralWidget(self.tboard)

        self.statusbar = self.statusBar()        
        self.tboard.msg2Statusbar[str].connect(self.statusbar.showMessage)
        
        self.tboard.start()
        self.showFullScreen()

        self.setWindowTitle('抽奖箱')        
        self.show()

        

class Board(QtGui.QFrame):

    TOTAL = 200
    
    msg2Statusbar = QtCore.pyqtSignal(str)

    Speed = 30
    Count = 0
    Interval = 1

    heartSnd = QtGui.QSound("heart.wav")

    def __init__(self, parent):
        super(Board, self).__init__(parent)
        
        self.TOTAL,ok = QInputDialog.getInt(self,"参加抽奖人数",
                                            "请输入人数",
                                            200,
                                            2,
                                            1000)
        
        self.initBoard()
        
        
    def initBoard(self):     

        self.timer = QtCore.QBasicTimer()
       
        self.setFocusPolicy(QtCore.Qt.StrongFocus)
        self.isStarted = False
        self.isPaused = False
        self.setFocus()

    def start(self):
        
        if self.isPaused:
            return

        self.isStarted = True

        self.msg2Statusbar.emit("starting")

        self.timer.start(Board.Speed, self)

        
    def pause(self):
        
        if not self.isStarted:
            return

        self.isPaused = not self.isPaused
        
        if self.isPaused:
            #self.timer.stop()
            self.heartSnd.play()
            self.msg2Statusbar.emit("generating number ...")
            
        else:

            self.Interval = 1
            self.timer.start(Board.Speed, self)
            self.msg2Statusbar.emit("running...")

        self.update()


        
    def paintEvent(self, event):
        screen = QtGui.QDesktopWidget().screenGeometry()
        
        painter = QtGui.QPainter(self)
        rect = self.contentsRect()

               
        R = random.randint(10,200)
        G = random.randint(10,200)
        B = random.randint(10,200)

        minSize = int(screen.height()*0.3)
        maxSize = int(screen.height()*0.5)
        SIZE = random.randint(minSize,maxSize)

        NUM = random.randint(1,self.TOTAL)

        if self.isPaused and not self.timer.isActive():
            R,G,B = 255,0,0
            SIZE = maxSize
        else:
            self.drawPoints(painter)
            
            
        painter.setPen(QtGui.QColor(R, G, B))
        
        painter.setFont(QtGui.QFont('Decorative', SIZE))
        painter.drawText(event.rect(), QtCore.Qt.AlignCenter, str(NUM))

        

    def drawPoints(self, qp):
      
        qp.setPen(QtCore.Qt.red)
        size = self.size()
        
        for i in range(1000):
            x = random.randint(1, size.width()-1)
            y = random.randint(1, size.height()-1)
            color = QtGui.QColor(random.randint(10,200),
                                 random.randint(10,200),
                                 random.randint(10,200))
            qp.setPen(color)
            qp.fillRect(x, y,random.randint(10,50),random.randint(10,50),color)
            
    def keyPressEvent(self, event):
        
        if not self.isStarted :
            super(Board, self).keyPressEvent(event)
            return

        key = event.key()
        
        if key == QtCore.Qt.Key_Space:
            self.pause()
            return
        if key == QtCore.Qt.Key_Escape:
            sys.exit()
            
        if self.isPaused:
            return
        else:
            super(Board, self).keyPressEvent(event)
                

    def timerEvent(self, event):
        
        if event.timerId() == self.timer.timerId():
            self.Count+=1
            if self.Count % self.Interval == 0:
                self.Count = 0
                self.update()
                #sound_file = "Pickup_Coin.wav"

                if self.isPaused:
                    self.Interval +=1
                    if self.Interval > 10:
                        self.timer.stop()
                        self.msg2Statusbar.emit("Congratulations ^_^ ...")
                else:
                    
                    self.Interval = 1
                

        else:
            super(Board, self).timerEvent(event)
            



def main():
    
    app = QtGui.QApplication([])
    lottery = Lottery()    
    sys.exit(app.exec_())


if __name__ == '__main__':
    main()
