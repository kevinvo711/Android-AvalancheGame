package afzal.mugrai.vo.avalanche;

/**
 * Created by afzal8690 on 6/7/2015.
 */
public class Player {

    float height, width, left, top, right, bot;

    public Player(int h, int w)
    {
        height = h;
        width = w;
    }

    public void setLeft(float l){
        left = l;
    }

    public void setTop(float t){
        top = t;
    }

    public void setRight(float r){
        right = r;
    }

    public void setBot(float b){
        bot = b;
    }

    //----------------------------------------------------------------------
    // GETTING VALUES
    //----------------------------------------------------------------------
    public float getLeft(){
        return left;
    }

    public float getTop()
    {
        return top;
    }

    public float getRight()
    {
        return right;
    }

    public float getBot(){
        return bot;
    }
}
