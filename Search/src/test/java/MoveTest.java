import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MoveTest {
    @Test
    public void conversionTest(){
        Move tempMove;
        for(int from=0;from<64;from++){
            for(int to=0;to<64;to++){
                for(int c=0;c<5;c++){
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
                        default:
                            fail();
                    }
                }
            }
        }
    }
}
