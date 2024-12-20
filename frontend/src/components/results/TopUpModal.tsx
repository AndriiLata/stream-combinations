import React from "react";
import type { User } from "../../context/UserContext";

interface TopUpModalProps {
  open: boolean;
  user: User | null;
  onClose: () => void;
  onTopUp: () => void;
}

const TopUpModal: React.FC<TopUpModalProps> = ({
  open,
  user,
  onClose,
  onTopUp,
}) => {
  if (!open) return null;

  return (
    <div className="modal modal-open">
      <div className="modal-box">
        <h3 className="font-bold text-lg">Not Enough CheckPoints</h3>
        <p className="mt-4">
          You don't have enough CheckPoints to buy this package.
        </p>
        <p className="mt-2">Your current balance: {user?.balance}</p>
        <div className="modal-action flex justify-between mt-4">
          <button className="btn btn-error" onClick={onClose}>
            Cancel
          </button>
          <button className="btn btn-warning" onClick={onTopUp}>
            Top Up CheckPoints
          </button>
        </div>
      </div>
    </div>
  );
};

export default TopUpModal;
