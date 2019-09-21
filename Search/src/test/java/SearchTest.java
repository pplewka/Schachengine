import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SearchTest {
    @Test
    public void getFullGeneratedDepthTest(){
        Move root = new MoveImpl(0,0,'0',null,false);

        Move [] rootchildren = new Move []{new MoveImpl(0,0,'0',null,false),
                                            new MoveImpl(0,0,'0',null,false)};

        root.setChildren(rootchildren);

        Move [] childChildren = new Move []{new MoveImpl(0,0,'0',null,false),
                                            new MoveImpl(0,0,'0',null,false)};

        rootchildren[0].setChildren(childChildren);

        SearchImpl.getSearch().setRoot(root);

        assertEquals(SearchImpl.getSearch().getFullGeneratedDepth(),1);



        root = new MoveImpl(0,0,'0',null,false);

        rootchildren = new Move []{new MoveImpl(0,0,'0',null,false),
                new MoveImpl(0,0,'0',null,false)};

        root.setChildren(rootchildren);

        Move [] child1Children = new Move []{new MoveImpl(0,0,'0',null,false),
                new MoveImpl(0,0,'0',null,false)};

        rootchildren[0].setChildren(child1Children);

        Move [] child2Children = new Move []{new MoveImpl(0,0,'0',null,false),
                new MoveImpl(0,0,'0',null,false)};

        rootchildren[1].setChildren(child2Children);

        SearchImpl.getSearch().setRoot(root);

        assertEquals(SearchImpl.getSearch().getFullGeneratedDepth(),2);
    }
}
