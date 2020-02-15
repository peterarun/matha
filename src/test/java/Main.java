import java.util.Scanner;

// Remember that the class name should be "Main" and should be "public".
public class Main
{
	public static void main(String[] args) throws Exception
	{
		Scanner s = new Scanner(System.in);
		int lineCnt = s.nextInt();

		int[][][] arr1 = new int[lineCnt][3][3];
		boolean[] res = new boolean[lineCnt];

		for (int i = 0; i < lineCnt; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					arr1[i][j][k] = s.nextInt();
				}
			}
			res[i] = isOrdered2(arr1[i]);
		}

		for (boolean re : res)
		{
			System.out.println(re ? "yes" : "no");
		}
	}

	public static boolean isOrdered2(int[][] arr1)
	{
		boolean ordMain = true;
		boolean[] eqRows = new boolean[]{true, true, true};

		int[] ordArr = null;
		for (int i = 0; i < 3; i++)
		{
			int[] indT = new int[]{arr1[0][i], arr1[1][i], arr1[2][i]};

			if(ordArr == null)
			{
				ordArr = fetchOrderArr(indT);
			}
			else
			{
				int[] ordArrNew = fetchOrderArr(indT);
				for (int j = 0; j < 3; j++)
				{
					if(ordArrNew[j] != ordArr[j])
					{
						if(!eqRows[ordArrNew[j] + ordArr[j] - 1])
						{
							ordMain = false;
							break;
						}
					}
				}
				if(!ordMain)
				{
					break;
				}
				ordArr = ordArrNew;
				eqRows[0] = eqRows[0] && (arr1[0][i] == arr1[1][i]);
				eqRows[1] = eqRows[1] && (arr1[0][i] == arr1[2][i]);
				eqRows[2] = eqRows[2] && (arr1[1][i] == arr1[2][i]);

			}
		}

		ordMain = ordMain && !(eqRows[0] || eqRows[1] || eqRows[2]);

		return ordMain;
	}

	public static int[] fetchOrderArr(int[] arrIn)
	{
		int[] arrOut = new int[]{0, 1, 2};
		for (int i = 2; i > 0; i--)
		{
			for (int j = 0;j < i; j++)
			{
				int idx = arrOut[j];
				int idxn = arrOut[j + 1];
				if(arrIn[idx] > arrIn[idxn])
				{
					int tmp = arrOut[j];
					arrOut[j] = arrOut[j + 1];
					arrOut[j + 1] = tmp;
				}
			}
		}

		return arrOut;
	}
}