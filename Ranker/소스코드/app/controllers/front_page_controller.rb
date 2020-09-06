class FrontPageController < ApplicationController
  def index
    if params[:s_id].nil? && params[:d_id].nil?
      @elements = Element.all
    elsif params[:s_id].nil?
      @elements = Element.where(detail_id: params[:d_id])
    else
      @elements = Element.where(subject_id: params[:s_id])
    end
  end
end
