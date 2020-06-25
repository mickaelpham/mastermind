# frozen_string_literal: true

require 'forwardable'
require 'ostruct'

class Differ
  extend Forwardable

  private_class_method :new

  def self.call(params)
    new(OpenStruct.new(params)).call
  end

  def call
    { greeting: greeting }
  end

  private

  def_delegators :@params, :name

  def initialize(params)
    @params = params
  end

  def greeting
    "Hello #{name}!"
  end
end
