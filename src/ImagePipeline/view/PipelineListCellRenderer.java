package ImagePipeline.view;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import ImagePipeline.model.primitives.ModulePrimitive;

class PipelineListCellRenderer extends JLabel implements ListCellRenderer<ModulePrimitive> {
    public PipelineListCellRenderer() {
        setOpaque(true);
    }

    // @Override
    // public Component getListCellRendererComponent(JList<? extends
    // ModulePrimitive> list, ModulePrimitive value,
    // int index, boolean isSelected, boolean cellHasFocus) {
    // String moduleName = value.getModuleName();
    // setText(moduleName);

    // setBackground(isSelected ? Color.blue : Color.white);
    // setForeground(isSelected ? Color.white : Color.black);
    // return this;
    // }

    @Override
    public Component getListCellRendererComponent(JList<? extends ModulePrimitive> list, ModulePrimitive value,
            int index, boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        String moduleName = value.getModuleName();
        setText(moduleName);

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        return this;
    }
}