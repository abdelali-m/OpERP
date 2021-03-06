package devopsdistilled.operp.client.items.panes.controllers.impl;

import javax.inject.Inject;

import devopsdistilled.operp.client.exceptions.EntityNameExistsException;
import devopsdistilled.operp.client.exceptions.NullFieldException;
import devopsdistilled.operp.client.items.exceptions.ProductBrandPairExistsException;
import devopsdistilled.operp.client.items.models.BrandModel;
import devopsdistilled.operp.client.items.models.ItemModel;
import devopsdistilled.operp.client.items.models.ProductModel;
import devopsdistilled.operp.client.items.panes.CreateItemPane;
import devopsdistilled.operp.client.items.panes.controllers.CreateItemPaneController;
import devopsdistilled.operp.client.items.panes.models.CreateItemPaneModel;
import devopsdistilled.operp.server.data.entity.items.Item;

public class CreateItemPaneControllerImpl implements CreateItemPaneController {

	@Inject
	private CreateItemPaneModel model;

	@Inject
	private CreateItemPane view;

	@Inject
	private ItemModel itemModel;

	@Inject
	private ProductModel productModel;

	@Inject
	private BrandModel brandModel;

	@Override
	public void init() {
		view.init();
		model.registerObserver(view);
		productModel.registerObserver(view);
		brandModel.registerObserver(view);
	}

	@Override
	public void validate(Item item) throws ProductBrandPairExistsException,
			EntityNameExistsException, NullFieldException {

		if (item.getItemName().equalsIgnoreCase(""))
			throw new NullFieldException("Item Name can't be empty");

		if (item.getProduct() == null)
			throw new NullFieldException(
					"Item must be associated with a product");

		if (item.getBrand() == null)
			throw new NullFieldException("Item must be associated with a brand");

		if (itemModel.getService().isProductBrandPairExists(item.getProduct(),
				item.getBrand()))
			throw new ProductBrandPairExistsException(
					"Product and Brand Pair already exists");

		if (itemModel.getService().isItemNameExists(item.getItemName()))
			throw new EntityNameExistsException("Item Name already exists");
	}

	@Override
	public Item save(Item item) {
		Item savedItem = itemModel.saveAndUpdateModel(item);
		return savedItem;
	}
}
