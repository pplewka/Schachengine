import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class KingTest {

    @Test
    public void emptyMovementWhite(){
        Board b = new BoardImpl();
        b.setField(Piece.WKING,28);
        b.setwKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] expected=new int[]{19,20,21,27,29,35,36,37};

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),expected.length);

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard=m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.WKING&&mboard.getPiece(28)==Piece.EMPTY&&mboard.iswKingMoved()){
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
        b.setField(Piece.BKING,28);
        b.setbKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] expected=new int[]{19,20,21,27,29,35,36,37};

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),expected.length);

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard=m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.BKING&&mboard.getPiece(28)==Piece.EMPTY&&mboard.isbKingMoved()){
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
        b.setField(Piece.WKING,28);
        b.setField(Piece.BROOK,11);
        b.setField(Piece.BROOK,13);
        b.setField(Piece.BROOK,18);
        b.setField(Piece.BROOK,34);
        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),0);
    }

    @Test
    public void selfCheckBlack(){
        Board b = new BoardImpl();
        b.setField(Piece.BKING,28);
        b.setField(Piece.WROOK,11);
        b.setField(Piece.WROOK,13);
        b.setField(Piece.WROOK,18);
        b.setField(Piece.WROOK,34);
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        assertEquals(li.size(),0);
    }

    @Test
    public void opponentBlockingWhite(){
        Board b = new BoardImpl();
        b.setField(Piece.WKING,28);
        b.setwKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,true);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] blocking=new int[]{19,20,21,27,29,35,36,37};
        for(int i:blocking){
            b.setField(Piece.BPAWN,i);
        }



        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        //only 5 because check
        assertEquals(li.size(),5);
        int [] expected=new int[]{19,20,21,35,37};

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard= m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.WKING&&mboard.getPiece(28)==Piece.EMPTY&&mboard.iswKingMoved()){
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
        b.setField(Piece.BKING,28);
        b.setbKingMoved(false);
        Move root = new MoveImpl(0,0,'0',b,false);
        MoveGeneration mg= MoveGenerationImpl.getMoveGeneration();

        int [] blocking=new int[]{19,20,21,27,29,35,36,37};
        for(int i:blocking){
            b.setField(Piece.WPAWN,i);
        }



        ArrayList<Move> li = new ArrayList<>();
        mg.generateKingMoves(root,66,li);

        //only 5 because check
        assertEquals(li.size(),5);
        int [] expected=new int[]{19,21,35,36,37};

        for(Move m:li){
            boolean found=false;
            for(int i:expected){
                Board mboard = m.getBoard();
                if(m.getTo()==i&&mboard.getPiece(i)==Piece.BKING&&mboard.getPiece(28)==Piece.EMPTY&&mboard.isbKingMoved()){
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
        b.setField(Piece.WKING,28);

        int [] blocking=new int[]{19,20,21,27,29,35,36,37};
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
        b.setField(Piece.BKING,28);

        int [] blocking=new int[]{19,20,21,27,29,35,36,37};
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
