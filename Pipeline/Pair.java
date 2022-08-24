public class Pair<T1, T2> 
{
    private T1 key;
    private T2 value;
    Pair(T1 key, T2 value)
    {
        this.key = key;
        this.value = value;
    }
    public T2 getValue()
    {
        return value;
    }
    public T1 getKey()
    {
        return key;
    }
}
