class CategoryController < ApplicationController
  def index
    Element.all.each do |e|
      e.average = e.estimations.average(:saticification).to_f
      e.save!
    end
    
    if params[:s_id].nil? && params[:d_id].nil?
      @elements = Element.all
    elsif params[:s_id].nil?
      @elements = Element.where(detail_id: params[:d_id])
    else
      @elements = Element.where(subject_id: params[:s_id])
    end
    
    # Element.joins(:detail).where()
    # Element.joins(:detail).joins(:subjects).where()
  end

  def show
    @element = Element.find(params[:id])
    @estimation = Estimation.new
    @estimations = @element.estimations
  end
end
