package cn.homjie.distributed.rabbit;

import cn.homjie.distributed.domain.DescriptionEntity;
import cn.homjie.distributed.operate.DescriptionService;

public class RecieveDescription extends RecieverAdapter<DescriptionEntity>{
	
	private DescriptionService descriptionService;
	
	public void setDescriptionService(DescriptionService descriptionService) {
		this.descriptionService = descriptionService;
	}

	@Override
	public void recieve(DescriptionEntity entity) {
		descriptionService.insert(entity);
	}

}
