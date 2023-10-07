package test;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckBoxCellRenderer extends DefaultTableCellRenderer {
    private JCheckBox checkBox = new JCheckBox();

    public CheckBoxCellRenderer() {
        super();
        setOpaque(true);
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);

        checkBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Alternar entre true y false cuando se hace clic en el checkbox
                checkBox.setSelected(!checkBox.isSelected());
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }
        return checkBox;
    }
}
