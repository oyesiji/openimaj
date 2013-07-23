package org.openimaj.math.matrix;


import ch.akuhn.matrix.Matrix;
import ch.akuhn.matrix.SparseMatrix;
import ch.akuhn.matrix.Vector;
import ch.akuhn.matrix.Vector.Entry;

/**
 * Some helpful operations on {@link Matrix} instances
 * @author Sina Samangooei (ss@ecs.soton.ac.uk)
 *
 */
public class MatlibMatrixUtils {

	/**
	 * @param mat
	 * @return uses {@link SparseMatrix#density()}
	 */
	public static double sparcity(SparseMatrix mat) {
		return 1 - mat.density();
	}

	/**
	 * Bring each element to the power d
	 * @param degree
	 * @param d
	 * @return the input
	 */
	public static <T extends Matrix>  T powInplace(T degree, double d) {
		int rowN = 0;
		for (Vector ent : degree.rows()) {
			for (Entry row : ent.entries()) {
				degree.put(rowN, row.index, Math.pow(row.value, d));
			}
			rowN++;
		}
		return degree;
	}

	/**
	 * @param D
	 * @param A
	 * @return R = D . A
	 */
	public static SparseMatrix times(DiagonalMatrix D, SparseMatrix A) {
		SparseMatrix mat = new SparseMatrix(A.rowCount(), A.columnCount());
		double[] Dvals = D.getVals();
		int rowIndex = 0;
		for (Vector row : A.rows()) {
			for (Entry ent : row.entries()) {
				mat.put(rowIndex, ent.index, ent.value * Dvals[rowIndex]);
			}
			rowIndex++;
		}
		return mat;
	}

	/**
	 * @param D
	 * @param A
	 * @return R =  A . D
	 */
	public static SparseMatrix times(SparseMatrix A,DiagonalMatrix D) {
		SparseMatrix mat = new SparseMatrix(A.rowCount(), A.columnCount());
		int rowIndex = 0;
		double[] Dvals = D.getVals();
		for (Vector row : A.rows()) {
			for (Entry ent : row.entries()) {
				mat.put(rowIndex, ent.index, ent.value * Dvals[ent.index]);
			}
			rowIndex++;
		}
		return mat;
	}

	/**
	 * @param D
	 * @param A
	 * @return the same matrix A
	 *
	 */
	public static <T extends Matrix> T minusInplace(DiagonalMatrix D, T A) {
		double[] Dval = D.getVals();
		for (int i = 0; i < Dval.length; i++) {
			A.put(i, i, A.get(i, i) - Dval[i]);
		}
		return A;
	}

	/**
	 * @param D
	 * @param A
	 * @return the same matrix A
	 *
	 */
	public static <T extends Matrix> T plusInplace(DiagonalMatrix D, T A) {
		double[] Dval = D.getVals();
		for (int i = 0; i < Dval.length; i++) {
			A.put(i, i, A.get(i, i) + Dval[i]);
		}
		return A;
	}



}
