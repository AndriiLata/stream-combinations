import React from "react";
import { StreamingPackage } from "../../context/SearchContext";

interface ConfirmationModalProps {
  open: boolean;
  packageToBuy: StreamingPackage | null;
  termsAccepted: boolean;
  onClose: () => void;
  onConfirm: () => void;
  onToggleTerms: () => void;
  onShowTerms: () => void;
}

const ConfirmationModal: React.FC<ConfirmationModalProps> = ({
  open,
  packageToBuy,
  termsAccepted,
  onClose,
  onConfirm,
  onToggleTerms,
  onShowTerms,
}) => {
  if (!open || !packageToBuy) return null;

  return (
    <div className="modal modal-open">
      <div className="modal-box bg-base-100 border border-base-300">
        <h3 className="font-bold text-xl">Confirm Purchase</h3>
        <p className="mt-4">
          Are you sure you want to subscribe to{" "}
          <span className="font-semibold">{packageToBuy.name}</span> for{" "}
          <span className="font-semibold">
            {packageToBuy.monthly_price_yearly_subscription_in_cents / 100}
          </span>{" "}
          CheckPoints?
        </p>
        <div className="mt-4 flex items-center gap-2">
          <input
            type="checkbox"
            className="checkbox"
            checked={termsAccepted}
            onChange={onToggleTerms}
          />
          <span className="text-sm">
            I have read and agree to the{" "}
            <button className="text-blue-600 underline" onClick={onShowTerms}>
              Terms and Agreements
            </button>
            .
          </span>
        </div>

        <div className="modal-action mt-6 flex justify-between">
          <button className="btn btn-error" onClick={onClose}>
            Cancel
          </button>
          <button
            className={`btn btn-warning ${!termsAccepted && "btn-disabled"}`}
            onClick={onConfirm}
          >
            Subscribe
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmationModal;
