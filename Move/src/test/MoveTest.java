import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MoveTest {
    @Test
    public void conversionTest(){
        Move tempMove;
        for(int from=0;from<64;from++){
            for(int to=0;to<64;to++){
                for(int c=0;c<6;c++){
                    switch (c){
                        case 0:
                            tempMove= new MoveImpl(from,to,' ',null,false);
                            assertEquals(from,tempMove.getFrom());
                            assertEquals(to,tempMove.getTo());
                            assertEquals(' ',tempMove.getChar());
                            break;
                        case 1:
                            tempMove= new MoveImpl(from,to,'Q',null,false);
                            assertEquals(from,tempMove.getFrom());
                            assertEquals(to,tempMove.getTo());
                            assertEquals('Q',tempMove.getChar());
                            break;
                        case 2:
                            tempMove= new MoveImpl(from,to,'R',null,false);
                            assertEquals(from,tempMove.getFrom());
                            assertEquals(to,tempMove.getTo());
                            assertEquals('R',tempMove.getChar());
                            break;
                        case 3:
                            tempMove= new MoveImpl(from,to,'K',null,false);
                            assertEquals(from,tempMove.getFrom());
                            assertEquals(to,tempMove.getTo());
                            assertEquals('K',tempMove.getChar());
                            break;
                        case 4:
                            tempMove= new MoveImpl(from,to,'B',null,false);
                            assertEquals(from,tempMove.getFrom());
                            assertEquals(to,tempMove.getTo());
                            assertEquals('B',tempMove.getChar());
                            break;
                        case 5:
                            tempMove= new MoveImpl(from,to,'0',null,false);
                            assertEquals(from,tempMove.getFrom());
                            assertEquals(to,tempMove.getTo());
                            assertEquals('0',tempMove.getChar());
                            break;
                        default:
                            fail();
                    }
                }
            }
        }
    }

    @Test
    public void setMaxMinIfBiggerSmallerTest(){
        //build evaluated tree for white Move
        Move leaf1 = new MoveImpl(0,0,'0',null,true);
        Move leaf2 = new MoveImpl(0,0,'0',null,true);
        Move leaf3 = new MoveImpl(0,0,'0',null,true);
        Move leaf4 = new MoveImpl(0,0,'0',null,true);

        Move mid1 = new MoveImpl(0,0,'0',null,false);
        Move mid2 = new MoveImpl(0,0,'0',null,false);

        Move root = new MoveImpl(0,0,'0',null,true);

        leaf1.setEval(70);
        leaf2.setEval(10);
        leaf1.setParent(mid1);
        leaf2.setParent(mid1);

        leaf3.setEval(50);
        leaf4.setEval(20);
        leaf3.setParent(mid2);
        leaf4.setParent(mid2);

        mid1.setParent(root);
        mid2.setParent(root);

        //calculate back
        calculateBack(leaf1);
        calculateBack(leaf2);
        calculateBack(leaf3);
        calculateBack(leaf4);

        //check tree
        assertEquals(50,root.getMaxMin());
        assertEquals(70,mid1.getMaxMin());
        assertEquals(50,mid2.getMaxMin());

        //build evaluated tree for black Move
        Move leaf11 = new MoveImpl(0,0,'0',null,false);
        Move leaf21 = new MoveImpl(0,0,'0',null,false);
        Move leaf31 = new MoveImpl(0,0,'0',null,false);
        Move leaf41 = new MoveImpl(0,0,'0',null,false);

        Move mid11 = new MoveImpl(0,0,'0',null,true);
        Move mid21 = new MoveImpl(0,0,'0',null,true);

        Move root1 = new MoveImpl(0,0,'0',null,false);

        leaf11.setEval(-70);
        leaf21.setEval(-10);
        leaf11.setParent(mid11);
        leaf21.setParent(mid11);

        leaf31.setEval(-50);
        leaf41.setEval(-20);
        leaf31.setParent(mid21);
        leaf41.setParent(mid21);

        mid11.setParent(root1);
        mid21.setParent(root1);

        //calculate back
        calculateBack(leaf11);
        calculateBack(leaf21);
        calculateBack(leaf31);
        calculateBack(leaf41);

        //check tree
        assertEquals(-50,root1.getMaxMin());
        assertEquals(-70,mid11.getMaxMin());
        assertEquals(-50,mid21.getMaxMin());
    }

    private void calculateBack(Move move){
        Move parent = move.getParent();
        boolean changed = true;
        while(parent!=null && changed){
            changed = parent.setMaxMinIfBiggerSmaller(move.getEval());
            parent = parent.getParent();
        }
    }
}
