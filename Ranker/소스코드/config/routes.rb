Rails.application.routes.draw do
  get 'elements/show'

  root 'front_page#index'
  resources :requests
  devise_for :users
  
  
  resources :elements do 
    resources :estimations, only: [:destroy, :create]
  end
  
  
  get 'category/index'

  get 'category/show/:id', to: 'category#show'
  
  get 'regist/loginpage'
  #get 'front_page/index'
  get '/login', to: 'regist#loginpage'
  get '/category', to: 'category#index'
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
