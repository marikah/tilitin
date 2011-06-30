package kirjanpito.db.postgresql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import kirjanpito.db.sql.SQLEntryDAO;

/**
 * <code>PSQLEntryDAO</code>:n avulla voidaan lisätä, muokata ja
 * poistaa vientejä sekä hakea olemassa olevien vientien
 * tietoja.
 *
 * @author Tommi Helineva
 */
public class PSQLEntryDAO extends SQLEntryDAO {
	private PSQLSession sess;

	public PSQLEntryDAO(PSQLSession sess) {
		this.sess = sess;
	}

	protected int getGeneratedKey() throws SQLException {
		return sess.getSequenceValue("entry_id_seq");
	}

	protected PreparedStatement getSelectByDocumentIdQuery() throws SQLException {
		return sess.prepareStatement("SELECT id, document_id, account_id, debit, amount, description, row_number FROM entry WHERE document_id = ? ORDER BY row_number");
	}

	protected PreparedStatement getSelectByDocumentIdsQuery(String documentIds) throws SQLException {
		return sess.prepareStatement("SELECT id, document_id, account_id, debit, amount, description, row_number FROM entry WHERE document_id IN (" + documentIds + ") ORDER BY row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByAccountAndNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN account a ON a.id = e.account_id INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY a.number, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdOrderByAccountAndDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN account a ON a.id = e.account_id INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? ORDER BY a.number, d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndAccountIdQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND e.account_id = ? ORDER BY d.date, d.number, e.row_number");
	}

	protected PreparedStatement getSelectByAccountIdQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE e.account_id = ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.date >= ? AND d.date <= ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndDateQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND d.date >= ? AND d.date <= ? ORDER BY d.number, e.row_number");
	}

	protected PreparedStatement getSelectByPeriodIdAndNumberQuery() throws SQLException {
		return sess.prepareStatement("SELECT e.id, e.document_id, e.account_id, e.debit, e.amount, e.description, e.row_number FROM entry e INNER JOIN document d ON d.id = e.document_id WHERE d.period_id = ? AND d.number BETWEEN ? AND ? ORDER BY e.document_id, e.row_number");
	}

	protected PreparedStatement getInsertQuery() throws SQLException {
		return sess.prepareStatement("INSERT INTO entry (id, document_id, account_id, debit, amount, description, row_number) VALUES (nextval('entry_id_seq'), ?, ?, ?, ?, ?, ?)");
	}

	protected PreparedStatement getUpdateQuery() throws SQLException {
		return sess.prepareStatement("UPDATE entry SET document_id=?, account_id=?, debit=?, amount=?, description=?, row_number=? WHERE id = ?");
	}

	protected PreparedStatement getDeleteQuery() throws SQLException {
		return sess.prepareStatement("DELETE FROM entry WHERE id = ?");
	}

	protected PreparedStatement getDeleteByPeriodIdQuery() throws SQLException {
		return sess.prepareStatement("DELETE FROM entry WHERE document_id IN (SELECT id FROM document WHERE period_id = ?)");
	}
}
