import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class KingTest {

    @Test
    public void emptyMovementWhite(){
        Board b = new BoardImpl();
        b.setField(Piece.WKING,66);
        b.setwKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] expected=new int[]{53,54,55,65,67,77,78,79};

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),expected.length);

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard=m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.WKING&&mboard.getPiece(66)==Piece.EMPTY&&mboard.iswKingMoved()){
                    found=true;
                }
            }

            if(!found){
                fail();
            }
        }
    }

    @Test
    public void emptyMovementBlack(){
        Board b = new BoardImpl();
        b.setField(Piece.BKING,66);
        b.setbKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] expected=new int[]{53,54,55,65,67,77,78,79};

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),expected.length);

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard=m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.BKING&&mboard.getPiece(66)==Piece.EMPTY&&mboard.isbKingMoved()){
                    found=true;
                }
            }

            if(!found){
                fail();
            }
        }
    }

    @Test
    public void selfCheckWhite(){
        Board b = new BoardImpl();
        b.setField(Piece.WKING,66);
        b.setField(Piece.BROOK,41);
        b.setField(Piece.BROOK,43);
        b.setField(Piece.BROOK,52);
        b.setField(Piece.BROOK,76);
        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),0);
    }

    @Test
    public void selfCheckBlack(){
        Board b = new BoardImpl();
        b.setField(Piece.BKING,66);
        b.setField(Piece.WROOK,41);
        b.setField(Piece.WROOK,43);
        b.setField(Piece.WROOK,52);
        b.setField(Piece.WROOK,76);
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),0);
    }

    @Test
    public void opponentBlockingWhite(){
        Board b = new BoardImpl();
        b.setField(Piece.WKING,66);
        b.setwKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] blocking=new int[]{53,54,55,65,67,77,78,79};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }



        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        //only 5 because check
        assertEquals(li.size(),5);
        int [] expected=new int[]{53,54,55,77,79};

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard= m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.WKING&&mboard.getPiece(66)==Piece.EMPTY&&mboard.iswKingMoved()){
                    found=true;
                }
            }

            if(!found){
                fail();
            }
        }
    }

    @Test
    public void opponentBlockingBlack(){
        Board b = new BoardImpl();
        b.setField(Piece.BKING,66);
        b.setbKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] blocking=new int[]{53,54,55,65,67,77,78,79};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }



        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        //only 5 because check
        assertEquals(li.size(),5);
        int [] expected=new int[]{53,55,77,78,79};

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard = m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.BKING&&mboard.getPiece(66)==Piece.EMPTY&&mboard.isbKingMoved()){
                    found=true;
                }
            }

            if(!found){
                fail();
            }
        }
    }

    @Test
    public void ownPiecesBlockingWhite(){
        Board b = new BoardImpl();
        b.setField(Piece.WKING,66);

        int [] blocking=new int[]{53,54,55,65,67,77,78,79};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }

        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),0);
    }

    @Test
    public void ownPiecesBlockingBlack(){
        Board b = new BoardImpl();
        b.setField(Piece.BKING,66);

        int [] blocking=new int[]{53,54,55,65,67,77,78,79};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),0);
    }
}
