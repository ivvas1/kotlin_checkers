import org.jetbrains.skia.*
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skiko.*
import java.awt.*
import java.awt.event.*
import java.lang.Math.abs
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import kotlin.system.exitProcess

enum class Checker{
    White, Black, Empty, WDamka, BDamka, MoveTrue, MoveTrueD
}

enum class Bpaint{
    DW, DB, Dmove, Dtake
}
enum class Player{
    White, Black, WinnerWhite, WinnerBlack
}

var moveB : Boolean = true
var turn : Player = Player.White
var xMove : Int = -1
var yMove : Int = -1
var CorrectTake : Boolean = false

val BlackD = Paint().apply {
    color = Color.makeRGB(117, 77, 9)
}

val WhiteD = Paint().apply {
    color = Color.makeRGB(255, 255, 160)
}
val Black = Paint().apply {
    color = Color.makeRGB(0, 0, 0)
}
val White = Paint().apply {
    color = Color.makeRGB(255, 255, 255)
}
val Red = Paint().apply {
    color = Color.makeRGB(255, 0, 0)
}
val Blue = Paint().apply {
    color = Color.makeRGB(69, 0, 255)
}
val Fiol = Paint().apply {
    color = Color.makeRGB(219, 77, 234)
}
val Brd = Paint().apply {
    color = Color.makeRGB(159, 77, 90)
}
fun GameQueue(p : Player) : Player{
    if(p == Player.White)return Player.Black
    return Player.White
}


var Checkers = mutableListOf<MutableList<Checker>> ()
var Board = mutableListOf<MutableList<Bpaint>> ()
fun takereal(t : Int, p : Int) : Int {
    var qw: Int = 0
    var tw : Int = 0
    if (Checkers[p][t] == Checker.MoveTrue) {
        if (turn == Player.White) {

            if ((p > 0) && (t > 0) && (Checkers[p - 1][t - 1] == Checker.Empty) && !CorrectTake) {
                    Board[p - 1][t - 1] = Bpaint.Dmove
                    tw = 2
            }
            println(Checkers[6][3])
            if ((t < 7) && (p > 0) && (Checkers[p - 1][t + 1] == Checker.Empty) && !CorrectTake) {
                    Board[p - 1][t + 1] = Bpaint.Dmove
                    tw = 2
            }

            if ((t > 1) && (p > 1) && (Checkers[p - 1][t - 1] == Checker.Black || Checkers[p - 1][t - 1] == Checker.BDamka) && (Checkers[p - 2][t - 2] == Checker.Empty)) {
                Board[p - 2][t - 2] = Bpaint.Dtake
                qw = 1
            }

            if ((t < 6) && (p > 1) && (Checkers[p - 1][t + 1] == Checker.Black || Checkers[p - 1][t + 1] == Checker.BDamka) && (Checkers[p - 2][t + 2] == Checker.Empty)) {
                Board[p - 2][t + 2] = Bpaint.Dtake
                qw = 1
            }

            if ((t > 1) && (p < 6) && (Checkers[p + 1][t - 1] == Checker.Black || Checkers[p + 1][t - 1] == Checker.BDamka) && (Checkers[p + 2][t - 2] == Checker.Empty)) {
                Board[p + 2][t - 2] = Bpaint.Dtake
                qw = 1
            }

            if ((t < 6) && (p < 6) && (Checkers[p + 1][t + 1] == Checker.Black || Checkers[p + 1][t + 1] == Checker.BDamka) && (Checkers[p + 2][t + 2] == Checker.Empty)) {
                Board[p + 2][t + 2] = Bpaint.Dtake
                qw = 1
            }
        }
     else {
         if ((t > 0) && (p < 7) && (Checkers[p + 1][t - 1] == Checker.Empty) && !CorrectTake) {
                Board[p + 1][t - 1] = Bpaint.Dmove
                tw = 2
         }

            if ((t < 7) && (p < 7) && (Checkers[p + 1][t + 1] == Checker.Empty) && !CorrectTake) {
                Board[p + 1][t + 1] = Bpaint.Dmove
                tw = 2
            }
        if (Checkers[p][t] == Checker.MoveTrue) {
            if ((t > 1) && (p > 1) && (Checkers[p - 1][t - 1] == Checker.White || Checkers[p - 1][t - 1] == Checker.WDamka) && (Checkers[p - 2][t - 2] == Checker.Empty)) {
                Board[p - 2][t - 2] = Bpaint.Dtake
                qw = 1
            }

            if ((t < 6) && (p > 1) && (Checkers[p - 1][t + 1] == Checker.White || Checkers[p - 1][t + 1] == Checker.WDamka) && (Checkers[p - 2][t + 2] == Checker.Empty)) {
                Board[p - 2][t + 2] = Bpaint.Dtake
                qw = 1
            }

            if ((t > 1) && (p < 6) && (Checkers[p + 1][t - 1] == Checker.White || Checkers[p + 1][t - 1] == Checker.WDamka) && (Checkers[p + 2][t - 2] == Checker.Empty)) {
                Board[p + 2][t - 2] = Bpaint.Dtake
                qw = 1
            }

            if ((t < 6) && (p < 6) && (Checkers[p + 1][t + 1] == Checker.White || Checkers[p + 1][t + 1] == Checker.WDamka) && (Checkers[p + 2][t + 2] == Checker.Empty)) {
                Board[p + 2][t + 2] = Bpaint.Dtake
                qw = 1
            }
        }

    }
}
    return (tw+qw)
}

///////////////////ХОДЫ + РУБКА ДАМКОЙ//////////////////
fun takerealDamka(t : Int, p : Int) : Int{
    var qw : Int = 0
    var tw : Int = 0
    if(Checkers[p][t] == Checker.MoveTrueD) {
        /////ЧЁРНАЯ ДАМКА//////////////
        if (turn == Player.Black) {
            var o: Boolean = false
            for (i in (p + 1)..7) {
                var y = t - i + p
                if (y >= 0) {
                    if(Checkers[i][y] == Checker.Black || Checkers[i][y] == Checker.BDamka)break
                    if (Checkers[i][y] != Checker.Empty && o) break
                    if (Checkers[i][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[i][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.Empty && o) {
                        Board[i][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.WDamka || Checkers[i][y] == Checker.White) {
                        y -= 1
                        if (y >= 0 && i < 7 && Checkers[i + 1][y] == Checker.Empty) {
                            Board[i + 1][y] = Bpaint.Dtake
                            qw = 1

                        }
                        o = true
                    }
                }
            }
            o = false
            for (i in 0..(p - 1)) {
                var y = t + i + 1
                var x = p - 1 - i
                if (y < 8) {
                    if(Checkers[x][y] == Checker.Black || Checkers[x][y] == Checker.BDamka)break
                    if (Checkers[x][y] != Checker.Empty && o) break
                    if (Checkers[x][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[x][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.Empty && o) {
                        Board[x][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.WDamka || Checkers[x][y] == Checker.White) {
                        y += 1
                        if (y < 8 && x > 0 && Checkers[x - 1][y] == Checker.Empty) {
                            Board[x - 1][y] = Bpaint.Dtake
                            qw = 1

                        }
                        o = true
                    }
                }
            }

            o = false

            for (i in (p + 1)..7) {
                var y = t - p + i
                if (y < 8) {
                    if(Checkers[i][y] == Checker.Black || Checkers[i][y] == Checker.BDamka)break
                    if (Checkers[i][y] != Checker.Empty && o) break
                    if (Checkers[i][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[i][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.Empty && o) {
                        Board[i][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.WDamka || Checkers[i][y] == Checker.White) {
                        y += 1
                        if (y < 8 && i < 7 && Checkers[i + 1][y] == Checker.Empty) {
                            Board[i + 1][y] = Bpaint.Dtake
                            qw = 1

                        }
                        o = true
                    }
                }
            }
            o = false
            for (i in 0..(p - 1)) {
                var y = t - i - 1
                var x = p - 1 - i
                if (y >= 0) {
                    if(Checkers[x][y] == Checker.Black || Checkers[x][y] == Checker.BDamka)break
                    if (Checkers[x][y] != Checker.Empty && o) break
                    if (Checkers[x][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[x][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.Empty && o) {
                        Board[x][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.WDamka || Checkers[x][y] == Checker.White) {
                        y -= 1
                        if (y >= 0 && x > 0 && Checkers[x - 1][y] == Checker.Empty) {
                            Board[x - 1][y] = Bpaint.Dtake
                            qw = 1

                        }
                        o = true
                    }
                }
            }
        }

        //////////////////////БЕЛАЯ ДАМКА////////////////////////
        else {
            var o: Boolean = false
            for (i in (p + 1)..7) {
                var y = t - i + p
                if (y >= 0) {
                    if(Checkers[i][y] == Checker.White || Checkers[i][y] == Checker.WDamka)break
                    if (Checkers[i][y] != Checker.Empty && o) break
                    if (Checkers[i][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[i][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.Empty && o) {
                        Board[i][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.BDamka || Checkers[i][y] == Checker.Black) {
                        y -= 1
                        if (y >= 0 && i < 7 && Checkers[i + 1][y] == Checker.Empty) {
                            Board[i + 1][y] = Bpaint.Dtake
                            qw = 1
                        }
                        o = true
                    }
                }
            }
            o = false
            for (i in 0..(p - 1)) {
                var y = t + i + 1
                var x = p - 1 - i
                if (y < 8) {
                    if(Checkers[x][y] == Checker.White || Checkers[x][y] == Checker.WDamka)break
                    if (Checkers[x][y] != Checker.Empty && o) break
                    if (Checkers[x][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[x][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.Empty && o) {
                        Board[x][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.BDamka || Checkers[x][y] == Checker.Black) {
                        y += 1
                        if (y < 8 && x > 0 && Checkers[x - 1][y] == Checker.Empty) {
                            Board[x - 1][y] = Bpaint.Dtake
                            qw = 1
                        }
                        o = true
                    }
                }
            }

            o = false

            for (i in (p + 1)..7) {
                var y = t - p + i
                if (y < 8) {
                    if(Checkers[i][y] == Checker.White || Checkers[i][y] == Checker.WDamka)break
                    if (Checkers[i][y] != Checker.Empty && o) break
                    if (Checkers[i][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[i][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.Empty && o) {
                        Board[i][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[i][y] == Checker.BDamka || Checkers[i][y] == Checker.Black) {
                        y += 1
                        if (y < 8 && i < 7 && Checkers[i + 1][y] == Checker.Empty) {
                            Board[i + 1][y] = Bpaint.Dtake
                            qw = 1
                        }
                        o = true
                    }
                }
            }
            o = false
            for (i in 0..(p - 1)) {
                var y = t - i - 1
                var x = p - 1 - i
                if (y >= 0) {
                    if(Checkers[x][y] == Checker.White || Checkers[x][y] == Checker.WDamka)break
                    if (Checkers[x][y] != Checker.Empty && o) break
                    if (Checkers[x][y] == Checker.Empty && !o && !CorrectTake) {
                        Board[x][y] = Bpaint.Dmove
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.Empty && o) {
                        Board[x][y] = Bpaint.Dtake
                        tw = 2
                    }
                    if (Checkers[x][y] == Checker.BDamka || Checkers[x][y] == Checker.Black) {
                        y -= 1
                        if (y >= 0 && x > 0 && Checkers[x - 1][y] == Checker.Empty) {
                            Board[x - 1][y] = Bpaint.Dtake
                            qw = 1
                        }
                        o = true
                    }
                }
            }
        }
    /////////////////////////////////////////////////////////////////////
        return (tw + qw)
    }
    return 0
}


//МЕНЯЕТ НАПРАВЛЕНИЕ ИГРЫ
fun reverse(){
    if(turn == Player.White)turn = Player.Black
    else turn = Player.White
}
//ДЕЛАЕТ ДОСКУ ПУСТОЙ///////
fun EmptyBoard(){
    for(i in 0 .. 7){
        for(j in 0 .. 7){
            if((i + j)%2 == 0)Board[i][j] = Bpaint.DW
            else Board[i][j] = Bpaint.DB
        }
    }
}
fun thIsMove(){
    var f : Boolean = false
    for(i in 0 .. 7){
        for(j in 0 .. 7){
            if(turn == Player.White){
                if(Checkers[i][j] == Checker.White) {
                    Checkers[i][j] = Checker.MoveTrue
                    if(takereal(j, i) != 0)f = true
                    Checkers[i][j] = Checker.White
                    EmptyBoard()
                }

                else if(Checkers[i][j] == Checker.WDamka) {
                    Checkers[i][j] = Checker.MoveTrueD
                    if(takerealDamka(j, i)!= 0)f = true
                    Checkers[i][j] = Checker.WDamka
                }
                EmptyBoard()
            }
            if(turn == Player.Black){
                if(Checkers[i][j] == Checker.Black) {
                    Checkers[i][j] = Checker.MoveTrue
                    if(takereal(j, i)!= 0)f = true
                    Checkers[i][j] = Checker.Black
                    EmptyBoard()
                }
                else if(Checkers[i][j] == Checker.BDamka) {
                    Checkers[i][j] = Checker.MoveTrueD
                    if(takerealDamka(j, i)!= 0)f = true
                    Checkers[i][j] = Checker.BDamka
                }
                EmptyBoard()
            }
        }
    }
    if(f == false){
        if(turn == Player.White)turn = Player.WinnerBlack
        turn = Player.WinnerWhite
    }
}
fun click(x : Int, y : Int){
    var t = (x/100).toInt()
    var p = (y/100).toInt()
    var Ch = Checkers[p][t]

    if(moveB) {
        CorrectTake = false
        var f : Boolean = false

        for(i in 0 .. 7){
            for(j in 0 .. 7){
                if(turn == Player.White){
                    if(Checkers[i][j] == Checker.White) {
                        Checkers[i][j] = Checker.MoveTrue
                        if(takereal(j, i)%2 == 1)CorrectTake = true
                        if(takereal(j, i)!=0)f = true
                        Checkers[i][j] = Checker.White
                        EmptyBoard()
                    }

                    else if(Checkers[i][j] == Checker.WDamka) {
                        Checkers[i][j] = Checker.MoveTrueD
                        if(takerealDamka(j, i)%2 == 1)CorrectTake = true
                        if(takerealDamka(j, i)!=0)f = true
                        Checkers[i][j] = Checker.WDamka
                    }
                    EmptyBoard()
                }
                if(turn == Player.Black){
                    if(Checkers[i][j] == Checker.Black) {
                        Checkers[i][j] = Checker.MoveTrue
                        if(takereal(j, i)%2 == 1)CorrectTake = true
                        if(takereal(j, i)!=0)f = true
                        Checkers[i][j] = Checker.Black
                        EmptyBoard()
                    }
                    else if(Checkers[i][j] == Checker.BDamka) {
                        Checkers[i][j] = Checker.MoveTrueD
                        if(takerealDamka(j, i)%2 == 1)CorrectTake = true
                        if(takerealDamka(j, i)!=0)f = true
                        Checkers[i][j] = Checker.BDamka
                    }
                    EmptyBoard()
                }
            }
        }

        if(f == false){
            if(turn == Player.White)turn = Player.WinnerWhite
            else turn = Player.WinnerBlack
        }
        println(CorrectTake)
        //////////////////////////ХОДЫ НЕ ДАМКОЙ///////////////////////////////////
        if((turn == Player.White && Ch == Checker.White) || (turn == Player.Black && Ch == Checker.Black)){
            Checkers[p][t] = Checker.MoveTrue
            xMove = p
            yMove = t
            var qw : Int = 0
            if(turn == Player.White) {
                qw = takereal(t, p)
                println(qw)
            }
            else {
                qw = takereal(t, p)
            }

            if(qw != 0){
                moveB = false
            }
            else{
                if(turn == Player.White)Checkers[p][t] = Checker.White
                else Checkers[p][t] = Checker.Black
            }
        }

        /////////////////////////////////////ХОДЫ ДАМКОЙ///////////////////////////////////////
        else if((turn == Player.White && Ch == Checker.WDamka) || (turn == Player.Black && Ch == Checker.BDamka)){
            Checkers[p][t] = Checker.MoveTrueD
            var k : Int = takerealDamka(t, p)
            println(k)
            println(Checkers[p][t])
            if(k != 0) {
                moveB = false
                Checkers[p][t] = Checker.MoveTrueD
                xMove = p
                yMove = t
            }

            else{
                if(turn == Player.White)Checkers[p][t] = Checker.WDamka
                else Checkers[p][t] = Checker.BDamka
            }
        }

    }

    ///////////////////////////ЕСЛИ СДЕЛАН ХОД////////////////
    else{
        //////////НЕ БЫЛА СРУБЛЕНА ШАШКА//////////////
        if(Board[p][t] == Bpaint.Dmove){
            Checkers[p][t] = Checkers[xMove][yMove]

            for(i in 0 .. (abs((p - xMove)) - 1)) {
                var h : Int
                var w : Int
                if(p < xMove)h = xMove - i
                else h = xMove + i
                if(t < yMove)w = yMove - i
                else w = yMove + i
                Checkers[h][w] = Checker.Empty
            }

            if(turn == Player.White){
                if(Checkers[p][t] == Checker.MoveTrueD)Checkers[p][t] = Checker.WDamka
                else if(p == 0)Checkers[p][t] = Checker.WDamka
                else Checkers[p][t] = Checker.White

            }
            if(turn == Player.Black){
                if(Checkers[p][t] == Checker.MoveTrueD)Checkers[p][t] = Checker.BDamka
                else if(p == 7)Checkers[p][t] = Checker.BDamka
                else Checkers[p][t] = Checker.Black
            }
            reverse()
            EmptyBoard()
            moveB = true
        }

        /////////////////БЫЛА СРУБЛЕНА ШАШКА///////////////////
        else if(Board[p][t] == Bpaint.Dtake){
            Checkers[p][t] = Checkers[xMove][yMove]
            for(i in 0 .. (abs((p - xMove)) - 1)) {
                var h : Int
                var w : Int
                if(p < xMove)h = xMove - i
                else h = xMove + i
                if(t < yMove)w = yMove - i
                else w = yMove + i
                Checkers[h][w] = Checker.Empty
            }

            if(turn == Player.White){
                if(Checkers[p][t] == Checker.MoveTrueD)Checkers[p][t] = Checker.WDamka
                else if(p == 0)Checkers[p][t] = Checker.WDamka
                else Checkers[p][t] = Checker.White

            }

            else{
                if(Checkers[p][t] == Checker.MoveTrueD)Checkers[p][t] = Checker.BDamka
                else if(p == 7)Checkers[p][t] = Checker.BDamka
                else Checkers[p][t] = Checker.Black
            }
            if(Checkers[p][t] == Checker.WDamka || Checkers[p][t] == Checker.BDamka)Checkers[p][t] = Checker.MoveTrueD
            else Checkers[p][t] = Checker.MoveTrue

            xMove = -1
            yMove = -1
            EmptyBoard()
            if(takereal(t, p) == 1 || takerealDamka(t, p) == 1 || takerealDamka(t, p) == 3){
                xMove = p
                yMove = t
            }
            else {
                EmptyBoard()
                if(turn == Player.White){
                    if(Checkers[p][t] == Checker.MoveTrueD)Checkers[p][t] = Checker.WDamka
                    else if(p == 0)Checkers[p][t] = Checker.WDamka
                    else Checkers[p][t] = Checker.White

                }
                else{
                    if(Checkers[p][t] == Checker.MoveTrueD)Checkers[p][t] = Checker.BDamka
                    else if(p == 7)Checkers[p][t] = Checker.BDamka
                    else Checkers[p][t] = Checker.Black
                }
                reverse()
                moveB = true
            }
        }
    }
}

fun main() {

    for(i in 0 .. 7){
        if(i == 5 || i == 7)Checkers.add(mutableListOf(Checker.White, Checker.Empty, Checker.White, Checker.Empty, Checker.White, Checker.Empty, Checker.White, Checker.Empty))
        if(i == 6)Checkers.add(mutableListOf(Checker.Empty, Checker.White, Checker.Empty, Checker.White, Checker.Empty, Checker.White, Checker.Empty, Checker.White))
        if(i == 3 || i == 4)Checkers.add(mutableListOf(Checker.Empty, Checker.Empty, Checker.Empty, Checker.Empty, Checker.Empty, Checker.Empty, Checker.Empty, Checker.Empty))
        if(i == 0 || i == 2)Checkers.add(mutableListOf(Checker.Empty, Checker.Black, Checker.Empty, Checker.Black, Checker.Empty, Checker.Black, Checker.Empty, Checker.Black))
        if(i == 1) Checkers.add(mutableListOf(Checker.Black, Checker.Empty, Checker.Black, Checker.Empty, Checker.Black, Checker.Empty, Checker.Black, Checker.Empty))
        if(i % 2 == 0)Board.add(mutableListOf(Bpaint.DW,Bpaint.DB,Bpaint.DW,Bpaint.DB,Bpaint.DW,Bpaint.DB,Bpaint.DW,Bpaint.DB))
        if(i % 2 != 0)Board.add(mutableListOf(Bpaint.DB,Bpaint.DW,Bpaint.DB,Bpaint.DW,Bpaint.DB,Bpaint.DW,Bpaint.DB,Bpaint.DW))
    }


    val skiaLayer = SkiaLayer()
    skiaLayer.skikoView = GenericSkikoView(skiaLayer, object : SkikoView {

        val face = FontMgr.default.matchFamiliesStyle(arrayOf("Menlo"), FontStyle.NORMAL)
        val font = Font(face, 15f)
        val fontText = Font(face, 50f)


        override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
            canvas.clear(Color.makeRGB(0, 8, 255))

            if(turn == Player.WinnerBlack || turn == Player.WinnerWhite){
                    exitProcess(0)
                    //canvas.drawString("Winner: ", (832).toFloat(), (350).toFloat(), fontText, Black)
                    //if(turn == Player.WinnerBlack)canvas.drawString("Black", (832).toFloat(), (410).toFloat(), fontText, Fiol)
                    //else canvas.drawString("Black", (832).toFloat(), (410).toFloat(), fontText, Brd)
            }
            for(i in 0..7){
                for(j in 0..7){
                    if((i + j) % 2 == 0) {
                        canvas.drawRRect(
                            RRect.Companion.makeLTRB(
                                (i * 100).toFloat(),
                                (j * 100).toFloat(),
                                (i * 100 + 100).toFloat(),
                                (j * 100 + 100).toFloat(),
                                0f
                            ), WhiteD
                        )
                    }
                    if((i+j)%2 == 1){
                        canvas.drawRRect(
                            RRect.Companion.makeLTRB(
                                (i * 100).toFloat(),
                                (j * 100).toFloat(),
                                (i * 100 + 100).toFloat(),
                                (j * 100 + 100).toFloat(),
                                0f
                            ), BlackD
                        )
                    }
                }
            }

            for(i in 0..7) {
                for (j in 0..7) {
                    if(Board[i][j] == Bpaint.Dmove || Board[i][j] == Bpaint.Dtake){
                        canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 10f, Blue)
                    }
                }
            }

            canvas.drawString("Move: ", (832).toFloat(), (350).toFloat(), fontText, Black)
            if(turn == Player.White)canvas.drawString("White", (832).toFloat(), (410).toFloat(), fontText, Fiol)
            else canvas.drawString("Black", (832).toFloat(), (410).toFloat(), fontText, Brd)


            for(i in 0..7){
                for(j in 0 .. 7){
                    if(Checkers[i][j] == Checker.Black)canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, Black)
                    if(Checkers[i][j] == Checker.White)canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, White)
                    if(Checkers[i][j] == Checker.BDamka){
                        canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, Black)
                        canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 15f, White)
                    }
                    if(Checkers[i][j] == Checker.WDamka){
                        canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, White)
                        canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 15f, Black)
                    }
                    if(Checkers[i][j] == Checker.MoveTrue){
                        if(turn == Player.White){
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, White)
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 10f, Red)
                        }
                        else {
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, Black)
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 10f, Red)
                        }
                    }
                    if(Checkers[i][j] == Checker.MoveTrueD){
                        if(turn == Player.White){
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, White)
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 15f, Black)
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 10f, Red)
                        }
                        else {
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 45f, Black)
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 15f, White)
                            canvas.drawCircle((j*100 + 50).toFloat(), (i*100 + 50).toFloat(), 10f, Red)
                        }
                    }
                    if(i == 0)canvas.drawString((j + 1).toString(), (3).toFloat(), (j * 100 + 97).toFloat(), font, Black)
                    if(j == 0){
                        val c : String = (65 + i).toChar().toString()
                        canvas.drawString(c, (i * 100 + 87).toFloat(), (15).toFloat(), font, Black)
                    }
                }
            }

        }
    })
    SwingUtilities.invokeLater {
        val window = JFrame("Checkers").apply {
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            preferredSize = Dimension(1000, 837)
        }

        val l: MouseListener = object : MouseListener {
            override fun mouseClicked(e: MouseEvent) {
                click(e.x, e.y)
            }

            override fun mouseExited(e: MouseEvent) {}
            override fun mousePressed(e: MouseEvent) {

            }

            override fun mouseEntered(e: MouseEvent) {}
            override fun mouseReleased(e: MouseEvent) {}
        }
        skiaLayer . addMouseListener (l)

        skiaLayer.attachTo(window.contentPane)
        skiaLayer.needRedraw()
        window.pack()
        window.isVisible = true
    }
}
