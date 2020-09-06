class EstimationsController < ApplicationController
  before_action :authenticate_user!, only: [:create, :destroy]
  def create
    estimation = Estimation.new
    estimation.element_id = params[:element_id]
    estimation.user = current_user
    estimation.comment = params[:estimation][:comment]
    estimation.saticification = params[:estimation][:saticification]
    
    estimation.save
    redirect_back(fallback_location: root_path)
  end

  
  
  def destroy
    estimation = Estimation.find(params[:id])
    estimation.destroy
    
    redirect_to category_index_path(params[:element_id])
  end
  
end
