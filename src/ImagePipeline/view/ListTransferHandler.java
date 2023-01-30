package ImagePipeline.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

import ImagePipeline.model.primitives.ModulePrimitive;

class ListTransferHandler extends TransferHandler {
    private int _index;
    private boolean _beforeIndex = false;
    private JList<ModulePrimitive> _pipelinesList = null;

    public ListTransferHandler(JList<ModulePrimitive> pipelinesList) {
        _pipelinesList = pipelinesList;
    }

    @Override
    public int getSourceActions(JComponent comp) {
        return MOVE;
    }

    @Override
    public Transferable createTransferable(JComponent comp) {
        _index = _pipelinesList.getSelectedIndex();
        return new StringSelection(_pipelinesList.getSelectedValue().getModuleName());
    }

    @Override
    public void exportDone(JComponent comp, Transferable trans, int action) {
        if (action == MOVE) {
            ArrayList<ModulePrimitive> model = getListModelArray();
            if (_beforeIndex)
                model.remove(_index + 1);
            else
                model.remove(_index);
            setListModelArray(model);
        }
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {

        try {
            ArrayList<ModulePrimitive> model = getListModelArray();
            ModulePrimitive s = (ModulePrimitive) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            model.add(dl.getIndex(), s);
            _beforeIndex = dl.getIndex() < _index ? true : false;
            setListModelArray(model);
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<ModulePrimitive> getListModelArray() {
        ListModel<ModulePrimitive> model = _pipelinesList.getModel();

        int nModules = model.getSize();
        ArrayList<ModulePrimitive> modules = new ArrayList<ModulePrimitive>();
        for (int iModule = 0; iModule < nModules; iModule++) {
            modules.add(model.getElementAt(iModule));
        }

        return modules;
    }

    public void setListModelArray(ArrayList<ModulePrimitive> moduleNames) {
        DefaultListModel<ModulePrimitive> model = new DefaultListModel<ModulePrimitive>();

        int nModules = moduleNames.size();
        for (int iModule = 0; iModule < nModules; iModule++) {
            model.addElement(moduleNames.get(iModule));
        }
        _pipelinesList.setModel(model);
    }
}