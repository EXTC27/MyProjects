class RequestsController < ApplicationController
  before_action :authenticate_user!, only: [:new, :create, :edit, :update, :destroy]
  
  def index
    @requests = Request.all
  end

  def new
    @request = Request.new
  end
  
  def create
    @request = Request.new(params.require(:request).permit(:title, :content, :user_id))
    
    if @request.save
      flash[:success] = 'Request가 작성되었습니다!'
      redirect_to request_url(@request.id)
    else
      render 'new'
    end
  end

  def show
    @request = Request.find params[:id]
  end

  def edit
    @request = Request.find params[:id]
  end
  
  def update
    @request = Request.find params[:id]
    if @request.update(params.require(:request).permit(:title, :content, :user_id))
      redirect_to request_url(@request)
    else
      redirect_to edit_request_url(@request)
    end
  end
  
  def destroy
    @request = Request.find params[:id]
    @request.destroy
    redirect_to requests_url
  end
end
